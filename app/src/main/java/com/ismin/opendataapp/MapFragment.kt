package com.ismin.opendataapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.google.maps.android.clustering.view.DefaultClusterRenderer


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var mMap: GoogleMap
    private lateinit var itemDao: ItemDao
    private var mapAlreadyset: Boolean = false

    //TO BE REMOVED
    lateinit var items: List<Item>

    // Declare a variable for the cluster manager.
    lateinit var mClusterManager: ClusterManager<ClusterItem>

    override fun onStart() {
        super.onStart()
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Create instance to connect to the dataBase
        itemDao = AppDataBase.getAppDatabase(this.requireContext())
            .getItemDao()
        items = itemDao.getAll()

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        if (!mapAlreadyset) {
            mapAlreadyset = true
            mMap = googleMap

            mMap.setOnInfoWindowClickListener(this)
            mClusterManager = ClusterManager<ClusterItem>(activity, mMap)
            val markerClusterRenderer =
                DefaultClusterRenderer<ClusterItem>(activity, googleMap, mClusterManager)
            mClusterManager.renderer = markerClusterRenderer
            mMap.setInfoWindowAdapter(mClusterManager.markerManager)
            val customInfoWindow = CustomMapMarkerWindow(this.context)
            mClusterManager.markerCollection.setOnInfoWindowAdapter(customInfoWindow)

            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(47.0, 2.5))) //France center
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f))
            mMap.setOnCameraIdleListener(mClusterManager)
            mMap.setOnMarkerClickListener(mClusterManager)
            addItemsOnMap()
        }
    }

    fun addItemsOnMap() {
        for ((index, item) in items.withIndex()) {
            val offsetItem = ClusterItem(item.lat, item.lng, item.titre, index.toString())
            mClusterManager.addItem(offsetItem)
        }
        mClusterManager.cluster()
    }

    fun refreshItems() {
        items = itemDao.getAll()
        addItemsOnMap()
    }

    override fun onInfoWindowClick(marker: Marker) {
        listener?.onItemClicked(items[marker.snippet.toInt()])
    }

    interface OnFragmentInteractionListener {
        fun onItemClicked(item: Item)
    }
}
