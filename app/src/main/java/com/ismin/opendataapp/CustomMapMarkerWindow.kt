package com.ismin.opendataapp

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_map_marker_window.view.*

class CustomMapMarkerWindow(val context: Context?) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }

    override fun getInfoContents(p0: Marker?): View {
        var mInfoView = (context as Activity).layoutInflater.inflate(R.layout.fragment_map_marker_window, null)

        mInfoView.f_map_window_title.text = p0?.title

        return mInfoView
    }
}