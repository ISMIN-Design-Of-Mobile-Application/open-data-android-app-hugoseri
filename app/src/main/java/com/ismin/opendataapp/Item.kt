package com.ismin.opendataapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item (
    val periode: String,
    val lieux: String,
    @SerializedName("url_de_l_archive")
    val url: String,
    val lieux_de_conservation: String,
    @SerializedName("coordonnees_geographique")
    val coordonnees: ArrayList<Double>,
    val legende: String,
    val titre: String,
    @SerializedName("url_de_la_capture_d_ecran")
    val apercu: String,
    val type: String
) : Serializable