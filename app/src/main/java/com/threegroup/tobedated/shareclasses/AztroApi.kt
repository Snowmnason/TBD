package com.threegroup.tobedated.shareclasses

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface AztroApi {
    @POST("api")
    fun getHoroscope(
        @Query("sign") sign: String,
        @Query("day") day: String
    ): Call<HoroscopeResponse>
}
