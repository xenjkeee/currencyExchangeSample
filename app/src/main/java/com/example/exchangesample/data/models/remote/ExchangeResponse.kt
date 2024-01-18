package com.example.exchangesample.data.models.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class ExchangeResponse(
    @SerializedName("base") val baseCurrency: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, BigDecimal>
)