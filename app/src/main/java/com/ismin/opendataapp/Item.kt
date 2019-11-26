package com.ismin.opendataapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var periode: String ="",
    var lieux: String= "",
    var url: String="",
    var lieux_de_conservation: String="",
    var lat:Double=0.0,
    var long:Double=0.0,
    var legende: String="",
    var titre: String="",
    var apercu: String?="",
    var type: String=""
) : Serializable