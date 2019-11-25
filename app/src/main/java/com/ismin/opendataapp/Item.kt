package com.ismin.opendataapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Item(
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
    var type: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
