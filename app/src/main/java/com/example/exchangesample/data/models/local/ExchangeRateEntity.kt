package com.example.exchangesample.data.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.exchangesample.data.local.BigDecimalConverter
import java.math.BigDecimal

@Entity(
    tableName = "exchange_rates",
    primaryKeys = ["from_currency", "to_currency"]
)
@TypeConverters(BigDecimalConverter::class)
data class ExchangeRateEntity(
    @ColumnInfo(name = "from_currency") val fromCurrency: String,
    @ColumnInfo(name = "to_currency") val toCurrency: String,
    @ColumnInfo(name = "rate") val rate: BigDecimal
)