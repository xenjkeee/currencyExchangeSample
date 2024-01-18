package com.example.exchangesample.data.remote

import android.util.Log
import com.example.exchangesample.data.local.datasource.LocalExchangeDatasource
import com.example.exchangesample.data.mappers.toExchangeRate
import com.example.exchangesample.data.mappers.toExchangeRateEntities
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ExchangeRepository @Inject constructor(
    private val remoteDatasource: RemoteDatasource,
    private val localExchangeDatasource: LocalExchangeDatasource,
) {
    suspend fun refreshRates() {
        val response = remoteDatasource.getExchangeRates()
        Log.d("ExchangeRepository", response.toString())
        val entities = response.toExchangeRateEntities()
        localExchangeDatasource.replaceRates(entities)
    }

    fun getExchangeRates(fromCurrency: String) =
        localExchangeDatasource.getExchangeRates(fromCurrency).map { list ->
            list.map { it.toExchangeRate() }
        }
}