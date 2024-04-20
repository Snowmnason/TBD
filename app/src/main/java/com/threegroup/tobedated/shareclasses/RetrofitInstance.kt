package com.threegroup.tobedated.shareclasses

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: AztroApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://aztro.sameerkumar.website/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AztroApi::class.java)
    }
}
