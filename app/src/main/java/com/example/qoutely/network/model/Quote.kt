package com.example.qoutely.network.model

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("quote") var quote: String? = null,
    @SerializedName("author") var author: String? = null,
    @SerializedName("description") var description: String? = null
)
