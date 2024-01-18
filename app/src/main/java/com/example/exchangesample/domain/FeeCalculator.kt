package com.example.exchangesample.domain

import java.math.BigDecimal

object FeeCalculator {
    fun calculateFee(
        transactionsCount: Int,
        amount: BigDecimal
    ): BigDecimal = when (transactionsCount) {
        in 0 until 5 -> BigDecimal.ZERO
        else -> amount * BigDecimal(0.007)
    }
}