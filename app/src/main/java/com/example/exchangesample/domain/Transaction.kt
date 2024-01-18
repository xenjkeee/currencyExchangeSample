package com.example.exchangesample.domain

import java.math.BigDecimal


data class Transaction(
    val fromCurrency: String,
    val fromAmount: BigDecimal,
    val toCurrency: String,
    val toAmount: BigDecimal,
    val fee: BigDecimal,
)