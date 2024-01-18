package com.example.exchangesample.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exchangesample.R
import com.example.exchangesample.domain.Balance
import com.example.exchangesample.ui.theme.ExchangeSampleTheme
import kotlinx.coroutines.launch
import java.math.BigDecimal


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainActivityScreen(
    state: MainScreenState,
    onEvent: (MainScreenEvent) -> Unit
) {
    Log.d("MainActivityScreen", "STATE=$state")
    var showAlert by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                onClick = {
                    showAlert = true
                    onEvent(MainScreenEvent.SubmitClicked(state))
                },
                enabled = state.from.amountValidation is AmountValidation.Valid
            ) {
                Text(text = stringResource(id = R.string.submit))
            }
        }
    ) { paddingValues ->

        var showBottomSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()

        if (showBottomSheet) ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            LazyColumn(content = {
                items(state.rates) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onEvent(MainScreenEvent.TargetCurrencyChanged(it.toCurrency))
                                scope
                                    .launch { sheetState.hide() }
                                    .invokeOnCompletion {
                                        if (!sheetState.isVisible) showBottomSheet = false
                                    }
                            }
                            .padding(12.dp),
                        text = it.toCurrency
                    )
                }
            })
        }

        if (showAlert) AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text(text = stringResource(id = R.string.currency_converted)) },
            text = { Text(text = state.toAlertMessage()) },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text(text = stringResource(id = R.string.done))
                }
            }
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.my_balances).uppercase()
            )
            Spacer(modifier = Modifier.height(12.dp))
            BalancesRow(state.balances)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.currency_exchange).uppercase()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                var amount by remember { mutableStateOf("") }
                OutlinedTextField(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .fillMaxWidth(),
                    value = amount,
                    onValueChange = {
                        amount = it
                        onEvent(MainScreenEvent.AmountChanged(it))
                    },
                    visualTransformation = AmountInputVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    label = {
                        Text(text = stringResource(id = R.string.sell))
                    },
                    leadingIcon = {
                        Icon(
                            tint = Color.Red,
                            imageVector = Icons.Outlined.KeyboardArrowUp,
                            contentDescription = stringResource(id = R.string.sell)
                        )
                    },
                    isError = state.from.amountValidation !is AmountValidation.Valid,
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = state.from.fromCurrency
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ToAmountDisabledInput(state)
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .clickable { showBottomSheet = true }
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    text = state.to.toCurrency
                )
            }
        }
    }
}

@Composable
private fun MainScreenState.toAlertMessage() = if (fee != BigDecimal.ZERO) stringResource(
    id = R.string.currency_converted_with_fee,
    from.fromAmount.formatAmount(),
    from.fromCurrency,
    to.toAmount.formatAmount(),
    to.toCurrency,
    fee.formatAmount(),
    from.fromCurrency
) else stringResource(
    id = R.string.currency_converted_no_fee,
    from.fromAmount.formatAmount(),
    from.fromCurrency,
    to.toAmount.formatAmount(),
    to.toCurrency
)

@Composable
private fun RowScope.ToAmountDisabledInput(state: MainScreenState) {
    OutlinedTextField(
        modifier = Modifier
            .align(Alignment.CenterVertically)
            .weight(1f)
            .fillMaxWidth(),
        value = state.to.toAmount.formatAmount(),
        onValueChange = {},
        label = {
            Text(text = stringResource(id = R.string.receive))
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                tint = Color.Green,
                contentDescription = stringResource(id = R.string.receive)
            )
        },
        enabled = false
    )
}

@Composable
private fun BalancesRow(balances: List<Balance>) = LazyRow(
    contentPadding = PaddingValues(horizontal = 14.dp),
    content = {
        itemsIndexed(balances) { i, it ->
            Text(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = "${it.balance.formatAmount()} ${it.currency}"
            )
            if (i < balances.lastIndex)
                Spacer(modifier = Modifier.width(24.dp))
        }
    }
)

private
val sampleState = MainScreenState(
    balances = listOf(
        Balance("EUR", BigDecimal(678)),
        Balance("USD", BigDecimal(12)),
        Balance("UAH", BigDecimal(404)),
    ),
    MainScreenState.From(
        fromCurrency = "EUR",
        fromAmount = BigDecimal(100),
        amountValidation = AmountValidation.Valid,
    ),
    MainScreenState.To(
        toCurrency = "USD",
        toAmount = BigDecimal(120),
    ),
    rates = emptyList(),
    fee = BigDecimal(0)
)


@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun PreviewMainActivityScreen() = ExchangeSampleTheme {
    MainActivityScreen(sampleState, {})
}