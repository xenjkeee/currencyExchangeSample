package com.example.exchangesample.data.remote

import com.example.exchangesample.data.models.remote.ExchangeResponse
import retrofit2.http.GET

interface ExchangeRatesService {
    @GET("tasks/api/currency-exchange-rates")
    suspend fun getExchangeRates(): Result<ExchangeResponse>
}