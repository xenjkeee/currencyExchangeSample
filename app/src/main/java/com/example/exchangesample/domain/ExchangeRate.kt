package com.example.exchangesample.domain

import java.math.BigDecimal

data class ExchangeRate(
    val fromCurrency: String,
    val toCurrency: String,
    val rate: BigDecimal
)