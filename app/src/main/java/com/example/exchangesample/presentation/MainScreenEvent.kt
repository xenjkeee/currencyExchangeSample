package com.example.exchangesample.presentation

sealed class MainScreenEvent {
    data class AmountChanged(val newAmount: String) : MainScreenEvent()
    data class TargetCurrencyChanged(val newCurrency: String) : MainScreenEvent()
    data class SubmitClicked(val state: MainScreenState) : MainScreenEvent()
}