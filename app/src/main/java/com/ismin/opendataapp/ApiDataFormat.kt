package com.ismin.opendataapp

import java.io.Serializable

data class ApiDataFormat(
    var nhits: Int,
    val records: List<ApiDataFields>
) : Serializable

data class ApiDataFields(
   var fields: Item
): Serializable