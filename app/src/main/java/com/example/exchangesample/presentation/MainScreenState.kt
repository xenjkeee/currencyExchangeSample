package com.example.exchangesample.presentation

import com.example.exchangesample.domain.Balance
import com.example.exchangesample.domain.ExchangeRate
import java.math.BigDecimal

data class MainScreenState(
    val balances: List<Balance>,
    val from: From,
    val to: To,
    val rates: List<ExchangeRate>,
    val fee: BigDecimal,
) {

    data class From(
        val fromAmount: BigDecimal,
        val fromCurrency: String,
        val amountValidation: AmountValidation,
    )

    data class To(
        val toCurrency: String,
        val toAmount: BigDecimal,
    )
}