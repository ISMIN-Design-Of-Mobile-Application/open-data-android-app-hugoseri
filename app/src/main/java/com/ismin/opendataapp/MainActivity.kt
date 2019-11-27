package com.ismin.opendataapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.BoringLayout
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

    var item = Item(
        0,
        "1918-1939",
        "Paris",
        "http://medias.sncf.com/sncfcom/open-data/archives/tr_sardo_1749.pdf", "SARDO",
        49.29, 4.23,
        "Ce document dresse un historique complet de la situation de ligne d'Hirson à Amagne des débuts de la guerre jusqu'à 1924. L'ensemble des destructions subies par cette ligne est indiqué. Plusieurs annexes (tableaux, plans, etc.) viennent illustrer le propos.",
        "Reconstitution des lignes détruites pendant le cours de la Guerre 1914 - 1918 : ligne d'Hirson à Amagne",
        "http://medias.sncf.com/sncfcom/open-data/thumb/tr_sardo_1749_thumb.jpg",
        "JPEG"
    )
    var item2 = Item(
        1,
        "1914-1918",
        "PARIS ILE-DE-France",
        "http://medias.sncf.com/sncfcom/open-data/thumb/thumb_tr_sardo_1751.png", "SARDO",
        48.856614, 2.3522219,
        "Cette série de clichés montre des femmes au travail dans différents ateliers et dépôts de la Compagnie des chemins de fer de Paris à Lyon et à la Méditerranée ainsi que dans des trains de banlieue. Ces photographies ont ensuite servi à illustrer l'agenda de la compagnie. LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGGGTTTTTTTTTTTTTEXTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        "Utilisation de la main-d'œuvre féminine dans les ateliers, les dépôts et les trains PLM",
        "http://medias.sncf.com/sncfcom/open-data/thumb/thumb_tr_sardo_1751.png",
        "PNG"
    )

    var listItems: ArrayList<Item> = arrayListOf(item, item2, item, item2)

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

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        val tabLayout: TabLayout = findViewById(R.id.a_main_tablayout)
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager: ViewPager = findViewById(R.id.a_main_viewpager)
        val adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)

        val bundle = Bundle()
        bundle.putSerializable(LIST_ITEM_INFO, listItems as Serializable)
        listFragment.arguments = bundle
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

        // API communication
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(SERVER_BASE_URL)
            .build()
        apiService = retrofit.create<ApiService>(ApiService::class.java)

        //Create instance to connect to the dataBase
        itemDao = AppDataBase.getAppDatabase(this)
            .getItemDao()
    }

    override fun onStart() {
        super.onStart()
        if(itemDao.getAll().isEmpty()) {
            retrieveAllInfoFromDataBase()
            Toast.makeText(
                this@MainActivity,
                "The data base has just benn filled",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            Toast.makeText(
                this@MainActivity,
                "The data base was not empty",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    //Called when data are finished to be added to the dataBase after request from API
    fun dataAddedToDataBase(){
        var allRows : List<Item> = itemDao.getAll()
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
                    if(itemApiObjectContainsNullData(it.fields)){
                        itemDao.insert(createItemFromItemApiData(it.fields))
                    }
                }
                dataAddedToDataBase()
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

    fun retrieveAllInfoFromDataBase() {
        apiService.getNbItemInAPI().enqueue(object : Callback<ApiDataFormat> {
            override fun onResponse(
                call: Call<ApiDataFormat>,
                response: Response<ApiDataFormat>
            ) {
                val apiData = response.body()
                if (apiData != null) {
                    getNbRowsDataFromApi(apiData.nhits)
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
                    "Impossible to retrieve info from API : $t",
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
            itemApi.id, itemApi.periode, itemApi.lieux, itemApi.url,
            itemApi.lieux_de_conservation, itemApi.coordonnees[0], itemApi.coordonnees[1],
            itemApi.legende, itemApi.titre, itemApi.apercu
        )
        return item
    }

    fun itemApiObjectContainsNullData(i: ItemApiData) : Boolean{
        return  i.id!=null && i.periode!=null  && i.lieux!=null && i.url!=null &&
                i.lieux_de_conservation!=null && i.legende!=null && i.titre!=null && i.apercu!=null
                && i.type!=null && i.coordonnees!=null
    }
}
