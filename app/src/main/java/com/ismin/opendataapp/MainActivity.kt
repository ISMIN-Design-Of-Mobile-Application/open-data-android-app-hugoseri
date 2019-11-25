package com.ismin.opendataapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

class MainActivity : AppCompatActivity(), ListFragment.OnFragmentInteractionListener, MapFragment.OnFragmentInteractionListener, InfosFragment.OnFragmentInteractionListener {

    var item = Item("1918-1939",
        "Paris",
        "http://medias.sncf.com/sncfcom/open-data/archives/tr_sardo_1749.pdf", "SARDO",
        arrayListOf(49.29, 4.23),
        "Ce document dresse un historique complet de la situation de ligne d'Hirson à Amagne des débuts de la guerre jusqu'à 1924. L'ensemble des destructions subies par cette ligne est indiqué. Plusieurs annexes (tableaux, plans, etc.) viennent illustrer le propos.",
        "Reconstitution des lignes détruites pendant le cours de la Guerre 1914 - 1918 : ligne d'Hirson à Amagne",
        "http://medias.sncf.com/sncfcom/open-data/thumb/tr_sardo_1749_thumb.jpg",
        "JPEG")
    var item2 = Item("1914-1918",
        "PARIS ILE-DE-France",
        "http://medias.sncf.com/sncfcom/open-data/thumb/thumb_tr_sardo_1751.png", "SARDO",
        arrayListOf(48.856614, 2.3522219),
        "Cette série de clichés montre des femmes au travail dans différents ateliers et dépôts de la Compagnie des chemins de fer de Paris à Lyon et à la Méditerranée ainsi que dans des trains de banlieue. Ces photographies ont ensuite servi à illustrer l'agenda de la compagnie. LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGGGTTTTTTTTTTTTTEXTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT",
        "Utilisation de la main-d'œuvre féminine dans les ateliers, les dépôts et les trains PLM",
        "http://medias.sncf.com/sncfcom/open-data/thumb/thumb_tr_sardo_1751.png",
        "PNG")

    var listItems: ArrayList<Item> = arrayListOf(item, item2, item, item2)

    var listFragment: ListFragment = ListFragment()
    var mapFragment: MapFragment = MapFragment()
    var infosFragment: InfosFragment = InfosFragment()
    lateinit var apiService : ApiService

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

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab){
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
    }

    override fun onStart() {
        super.onStart()
        retrieveAllInfoFromDataBase()
    }


    fun getNbRowsDataFromApi(nbRows : Int){
        var allRows : List<ApiDataFields>? = emptyList()
        apiService.getAllDtata(nbRows).enqueue(object : Callback<ApiDataFormat> {
                override fun onResponse(
                    call: Call<ApiDataFormat>,
                    response: Response<ApiDataFormat>
                ) {
                    val apiData = response.body()
                    allRows = apiData?.records
                }

                override fun onFailure(call: Call<ApiDataFormat>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "Impossible to retrieve info from API : $t",
                        Toast.LENGTH_SHORT
                    ).show()
                    println(t)
                }
            })
    }

    fun retrieveAllInfoFromDataBase(){
        apiService.getNbItemInAPI().enqueue(object : Callback<ApiDataFormat> {
            override fun onResponse(
                call: Call<ApiDataFormat>,
                response: Response<ApiDataFormat>
            ) {
                val apiData = response.body()
                if(apiData != null) {
                    getNbRowsDataFromApi(apiData.nhits)
                }else{
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
}
