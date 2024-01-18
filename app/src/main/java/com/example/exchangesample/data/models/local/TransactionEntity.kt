package com.example.exchangesample.data.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.exchangesample.data.local.BigDecimalConverter
import java.math.BigDecimal


@Entity(tableName = "transactions")
@TypeConverters(BigDecimalConverter::class)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "from_currency") val fromCurrency: String,
    @ColumnInfo(name = "fromAmount") val fromAmount: BigDecimal,
    @ColumnInfo(name = "to_currency") val toCurrency: String,
    @ColumnInfo(name = "to_amount") val toAmount: BigDecimal,
    @ColumnInfo(name = "fee") val fee: BigDecimal,
)
