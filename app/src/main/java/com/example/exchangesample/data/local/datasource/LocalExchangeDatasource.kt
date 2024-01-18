package com.example.exchangesample.data.local.datasource

import com.example.exchangesample.data.local.dao.ExchangeRateDao
import com.example.exchangesample.data.models.local.ExchangeRateEntity
import javax.inject.Inject

class LocalExchangeDatasource @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
) {
    fun getExchangeRates(fromCurrency: String) =
        exchangeRateDao.getExchangeRates(fromCurrency)

    suspend fun replaceRates(rates: List<ExchangeRateEntity>) =
        exchangeRateDao.replaceAll(rates)


}