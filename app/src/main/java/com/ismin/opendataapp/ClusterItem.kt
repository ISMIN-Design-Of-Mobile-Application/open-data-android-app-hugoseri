package com.ismin.opendataapp

import com.google.android.gms.maps.model.LatLng

import com.google.maps.android.clustering.ClusterItem


class ClusterItem : ClusterItem {
    private val mPosition: LatLng
    var mTitle: String = ""
    var mSnippet: String = ""
    var mIndex: Int = 0

    constructor(lat: Double, lng: Double) {
        mPosition = LatLng(lat, lng)
    }

    constructor(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        index: Int
    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mIndex = index
    }

    override fun getPosition(): LatLng {
        return mPosition
    }

    override fun getTitle(): String {
        return mTitle
    }

    override fun getSnippet(): String {
        return mSnippet
    }

    fun getIndex():Int{
        return mIndex
    }
}
