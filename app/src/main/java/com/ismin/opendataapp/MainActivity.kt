package com.ismin.opendataapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentInteractionListener,
    MapFragment.OnFragmentInteractionListener, InfosFragment.OnFragmentInteractionListener {

    var listFragment: ListFragment = ListFragment()
    var mapFragment: MapFragment = MapFragment()
    var infosFragment: InfosFragment = InfosFragment()
    lateinit var apiService: ApiService
    private lateinit var itemDao: ItemDao

    override fun onItemClicked(item: Item) {
        val itemIntent = Intent(this, ItemActivity::class.java)
        itemIntent.putExtra(ITEM_INFO, item as Serializable)
        this.startActivity(itemIntent)
    }

    override fun displayURL(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        val chooser = Intent.createChooser(intent, "Ouvrir avec")

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                Toast.makeText(this, "Actualisation des données", Toast.LENGTH_SHORT).show()
                maybeRequestAPI()
                listFragment.refreshItems()
                true
            }
            // If we got here, the user's action was not recognized.
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val tabLayout: TabLayout = findViewById(R.id.a_main_tablayout)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        // API communication
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SERVER_BASE_URL)
            .build()
        apiService = retrofit.create<ApiService>(ApiService::class.java)

        //Create instance to connect to the dataBase
        itemDao = AppDataBase.getAppDatabase(this).getItemDao()

        maybeRequestAPI()

        val viewPager: ViewPager = findViewById(R.id.a_main_viewpager)
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)


        adapter.addFragment(listFragment, "Liste")

        adapter.addFragment(mapFragment, "Carte")

        adapter.addFragment(infosFragment, "Infos")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    fun maybeRequestAPI() {
        retrieveAllInfoFromAPI()
    }

    fun getNbRowsDataFromApi(nbRows: Int) {
        apiService.getAllDtata(nbRows).enqueue(object : Callback<ApiDataFormat> {
            override fun onResponse(
                call: Call<ApiDataFormat>,
                response: Response<ApiDataFormat>
            ) {
                val apiData = response.body()
                val allFields= apiData?.records
                allFields!!.forEach {
                    setExtenstiontoItemFromItem(it.fields)
                    if(itemApiObjectDoNotContainsNullData(it.fields)){
                        itemDao.insert(createItemFromItemApiData(it.fields))
                    }
                }
                mapFragment.refreshItems()
                listFragment.refreshItems()
            }

            override fun onFailure(call: Call<ApiDataFormat>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Impossible to retrieve info from API : $t",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun retrieveAllInfoFromAPI() {
        apiService.getNbItemInAPI().enqueue(object : Callback<ApiDataFormat> {
            override fun onResponse(
                call: Call<ApiDataFormat>,
                response: Response<ApiDataFormat>
            ) {
                val apiData = response.body()
                if (apiData != null) {
                    val nbRowsInApiPreviousCall = getPreferences(Context.MODE_PRIVATE).getInt(NB_ROWS_IN_API, -1)
                    if(apiData.nhits != nbRowsInApiPreviousCall) {
                        val editor = getPreferences(Context.MODE_PRIVATE).edit()
                        editor.putInt(NB_ROWS_IN_API, apiData.nhits)
                        editor.apply()
                        getNbRowsDataFromApi(apiData.nhits)
                        Toast.makeText(
                            this@MainActivity,
                            "Actualisation des données.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            this@MainActivity,
                            "Les données sont à jours.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Error while retrieving the number of rows in API's dataset.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onFailure(call: Call<ApiDataFormat>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Impossible d'actualiser les données. Verifier votre connexion internet.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun findExtensionFromUrl(url: String): String {
        var index: Int = url.length - 1
        var type : String = ""
        while (url[index] != '.'){
            index--
        }
        for(i in (index+1)until(url.length)) type+=url[i]
        return type.toUpperCase()
    }

    fun setExtenstiontoItemFromItem(item :ItemApiData) {
        item.type = findExtensionFromUrl(item.url)
    }

    fun createItemFromItemApiData(itemApi : ItemApiData) : Item{
        var item: Item = Item(
            itemApi.id, itemApi.periode, onlyFirstLetterUpperCase(itemApi.lieux), httpToHttps(itemApi.url),
            itemApi.lieux_de_conservation, itemApi.coordonnees[0], itemApi.coordonnees.get(1),
            itemApi.legende, itemApi.titre, httpToHttps(itemApi.apercu), itemApi.type
        )
        return item
    }

    fun itemApiObjectDoNotContainsNullData(i: ItemApiData) : Boolean{
        return  i.id!=null && i.periode!=null  && i.lieux!=null && i.url!=null &&
                i.lieux_de_conservation!=null && i.legende!=null && i.titre!=null && i.apercu!=null
                && i.type!=null && i.coordonnees!=null
    }
}
