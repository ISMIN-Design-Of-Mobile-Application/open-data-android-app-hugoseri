package com.ismin.opendataapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Item(
    val periode: String,
    val lieux: String,
    val url: String,
    val lieux_de_conservation: String,
    val lat:Double,
    val long:Double,
    val legende: String,
    val titre: String,
    val apercu: String,
    var type: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
