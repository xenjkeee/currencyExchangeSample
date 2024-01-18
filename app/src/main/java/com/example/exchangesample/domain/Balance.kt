package com.example.exchangesample.domain

import java.math.BigDecimal

data class Balance(
    val currency: String,
    val balance: BigDecimal
)