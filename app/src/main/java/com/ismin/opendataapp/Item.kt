package com.ismin.opendataapp

import java.io.Serializable

data class Item (
    val periode: String,
    val lieux: String,
    val url: String,
    val lieux_de_conservation: String,
    val lat: Double,
    val long: Double,
    val legende: String,
    val titre: String,
    val apercu: String,
    val type: String
) : Serializable