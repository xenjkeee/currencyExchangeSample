package com.example.exchangesample.data.mappers

import com.example.exchangesample.data.models.local.BalanceEntity
import com.example.exchangesample.data.models.local.ExchangeRateEntity
import com.example.exchangesample.data.models.local.TransactionEntity
import com.example.exchangesample.data.models.remote.ExchangeResponse
import com.example.exchangesample.domain.Balance
import com.example.exchangesample.domain.Transaction

fun ExchangeResponse.toExchangeRateEntities() = rates.map {
    ExchangeRateEntity(
        fromCurrency = baseCurrency,
        toCurrency = it.key,
        rate = it.value
    )
}

fun Balance.toBalanceEntity() = BalanceEntity(
    currency = currency,
    balance = balance
)

fun Transaction.toTransactionEntity() = TransactionEntity(
    fromCurrency = fromCurrency,
    fromAmount = fromAmount,
    toCurrency = toCurrency,
    toAmount = toAmount,
    fee = fee,
)