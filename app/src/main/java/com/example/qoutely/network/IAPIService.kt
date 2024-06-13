package com.example.qoutely.network

import com.example.qoutely.network.model.Quote
import com.example.qoutely.network.model.QuoteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface IAPIService {
    @Headers(
        "X-RapidAPI-Key: 9aba98e8d1msh95d505795d6ca6bp1771d8jsn29dff54f35d4",
        "X-RapidAPI-Host: get-quotes-api.p.rapidapi.com"
    )
    @GET("quotes")
    suspend fun getAllQoutes(): QuoteResponse

    @Headers(
        "X-RapidAPI-Key: 9aba98e8d1msh95d505795d6ca6bp1771d8jsn29dff54f35d4",
        "X-RapidAPI-Host: get-quotes-api.p.rapidapi.com"
    )
    @GET("category/{categoryName}")
    suspend fun getQuotesByCategory(
        @Path("categoryName") categoryName: String
    ): QuoteResponse
}