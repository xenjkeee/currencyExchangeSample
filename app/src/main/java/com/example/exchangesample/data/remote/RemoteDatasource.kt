package com.example.exchangesample.data.remote

import javax.inject.Inject

class RemoteDatasource @Inject constructor(
    private val service: ExchangeRatesService
) {
    suspend fun getExchangeRates() = service.getExchangeRates().fold(
        onSuccess = { it },
        onFailure = { throw it }
    )
}