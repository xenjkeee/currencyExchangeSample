package com.example.exchangesample.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.exchangesample.data.local.repository.BalanceRepository
import com.example.exchangesample.data.local.repository.TransactionRepository
import com.example.exchangesample.data.remote.ExchangeRepository
import com.example.exchangesample.domain.Balance
import com.example.exchangesample.domain.FeeCalculator
import com.example.exchangesample.domain.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val exchangeRepository: ExchangeRepository,
    private val balanceRepository: BalanceRepository,
    private val transactionRepository: TransactionRepository,
) : ViewModel() {


    private fun refreshExchangeRates() = viewModelScope.launch {
        while (true) {
            exchangeRepository.refreshRates()
            delay(REFRESH_DELAY)
        }
    }

    init {
        refreshExchangeRates()
    }

    private val fromAmountText = stateHandle.getStateFlow("fromAmountText", "")
    private val baseCurrency = stateHandle.getStateFlow("baseCurrency", "EUR")
    private val targetCurrency = stateHandle.getStateFlow("targetCurrency", "USD")

    // all exchange rates for given `baseCurrency`
    private val exchangeRates = baseCurrency.flatMapLatest {
        exchangeRepository.getExchangeRates(baseCurrency.value)
    }

    // get all balances
    private val balances = balanceRepository.getBalances()

    // current available balance for selected base currency
    private val selectedBaseCurrencyBalance = combine(
        baseCurrency, balances
    ) { currency, balances ->
        balances.find { it.currency == currency }?.balance ?: BigDecimal.ZERO
    }

    // number representation of `fromAmount`
    private val fromAmount = fromAmountText.map {
        try {
            BigDecimal(it).movePointLeft(2)
        } catch (e: NumberFormatException) {
            BigDecimal.ZERO
        }
    }
    private val transactionsCount = transactionRepository.getCount()
    private val fee = combine(
        transactionsCount, fromAmount
    ) { transactionsCount, amount ->
        FeeCalculator.calculateFee(transactionsCount, amount)
    }

    //amount validation: 0 < amount && amount + fee <= balance
    private val amountValidation = combine(
        selectedBaseCurrencyBalance, fromAmount, fee
    ) { balance, amount, fee ->
        when {
            amount == BigDecimal.ZERO || amount + fee > balance -> AmountValidation.Invalid
            else -> AmountValidation.Valid
        }
    }

    private val fromInfo = combine(
        fromAmount, baseCurrency, amountValidation
    ) { amount, currency, validation ->
        MainScreenState.From(amount, currency, validation)
    }

    private val targetExchangeRate = combine(
        targetCurrency, exchangeRates
    ) { currency, rates ->
        rates.find { it.toCurrency == currency }?.rate ?: BigDecimal.ONE
    }

    private val toInfo = combine(
        fromAmount, targetCurrency, targetExchangeRate
    ) { amount, currency, rate ->
        MainScreenState.To(currency, amount * rate)
    }

    val state = combine(
        balances, fromInfo, toInfo, exchangeRates, fee
    ) { balances, from, to, rates, fee ->
        MainScreenState(balances, from, to, rates, fee)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MainScreenState(
            balances = emptyList(),
            from = MainScreenState.From(
                fromAmount = BigDecimal.ZERO,
                fromCurrency = "EUR",
                amountValidation = AmountValidation.Invalid
            ),
            to = MainScreenState.To(
                toCurrency = "USD",
                toAmount = BigDecimal.ZERO,
            ),
            rates = emptyList(),
            fee = BigDecimal.ZERO
        )
    )

    //handle any ui event
    fun onEvent(event: MainScreenEvent) = when (event) {
        is MainScreenEvent.AmountChanged -> stateHandle.set(
            "fromAmountText", event.newAmount
        )

        is MainScreenEvent.TargetCurrencyChanged -> stateHandle.set(
            "targetCurrency", event.newCurrency
        )

        is MainScreenEvent.SubmitClicked -> {
            viewModelScope.launch {
                //get current balance and update value
                event.state.balances.find { it.currency == event.state.from.fromCurrency }
                    ?.let { old ->
                        val sum = event.state.from.fromAmount + event.state.fee
                        old.copy(balance = old.balance - sum)
                    }?.let { balanceRepository.setBalance(it) }
                //update target balance or create new
                (event.state.balances.find { it.currency == event.state.to.toCurrency }
                    ?: Balance(event.state.to.toCurrency, BigDecimal.ZERO)).let { old ->
                    old.copy(balance = old.balance + event.state.to.toAmount)
                }.let { balanceRepository.setBalance(it) }

                transactionRepository.insert(
                    Transaction(
                        fromCurrency = event.state.from.fromCurrency,
                        fromAmount = event.state.from.fromAmount,
                        toCurrency = event.state.to.toCurrency,
                        toAmount = event.state.to.toAmount,
                        fee = event.state.fee
                    )
                )
            }
            Unit
        }
    }

}

private const val REFRESH_DELAY = 5_000L
