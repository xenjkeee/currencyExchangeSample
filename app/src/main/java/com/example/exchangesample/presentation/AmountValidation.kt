package com.example.exchangesample.presentation

sealed class AmountValidation {
    object Valid : AmountValidation()
    object Invalid : AmountValidation()
}