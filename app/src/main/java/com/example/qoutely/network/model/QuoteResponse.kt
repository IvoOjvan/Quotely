package com.example.qoutely.network.model

import com.google.gson.annotations.SerializedName

data class QuoteResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("Quotes") var qoutes: ArrayList<Quote>? = arrayListOf()
)
