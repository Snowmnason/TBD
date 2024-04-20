package com.threegroup.tobedated.shareclasses.api.horoscope

import com.threegroup.tobedated.shareclasses.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun fetchHoroscope(sign: String, day: String) {
    RetrofitInstance.api.getHoroscope(sign, day).enqueue(object : Callback<HoroscopeResponse> {
        override fun onResponse(call: Call<HoroscopeResponse>, response: Response<HoroscopeResponse>) {
            if (response.isSuccessful) {
                response.body()?.let {
                    // Horoscope data should go here
                    println("Horoscope: ${it.description}")
                }
            } else {
               println("Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<HoroscopeResponse>, t: Throwable) {
            //println("Failed to retrieve horoscope: ${t.message}")
        }
    })
}
