package com.example.qoutely.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitClient {
    companion object{
        private var apiService: IAPIService? = null
        fun getInstance(): IAPIService{
            if(apiService == null){
                apiService = Retrofit
                    .Builder()
                    .baseUrl("https://get-quotes-api.p.rapidapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create()
            }
            return apiService!!
        }
    }
}