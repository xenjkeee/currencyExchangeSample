package com.example.exchangesample.data.mappers

import com.example.exchangesample.data.models.local.BalanceEntity
import com.example.exchangesample.data.models.local.ExchangeRateEntity
import com.example.exchangesample.domain.Balance
import com.example.exchangesample.domain.ExchangeRate

fun ExchangeRateEntity.toExchangeRate() = ExchangeRate(fromCurrency, toCurrency, rate)

fun BalanceEntity.toBalance() = Balance(
    currency = currency,
    balance = balance
)