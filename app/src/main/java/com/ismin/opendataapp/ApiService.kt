package com.ismin.opendataapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/records/1.0/search/?dataset=archives-sncf-new%40datasncf&sort=-id")
    fun getAllDtata(@Query("rows") nbRows : Int): Call<ApiDataFormat>
    @GET("api/records/1.0/search/?dataset=archives-sncf-new%40datasncf&rows=1")
    fun getNbItemInAPI(): Call<ApiDataFormat>
}