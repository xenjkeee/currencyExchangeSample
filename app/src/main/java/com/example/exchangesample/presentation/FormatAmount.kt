package com.example.exchangesample.presentation

import java.math.BigDecimal
import java.text.DecimalFormat

fun BigDecimal.formatAmount() = DecimalFormat("###,###,##0.00").format(this)