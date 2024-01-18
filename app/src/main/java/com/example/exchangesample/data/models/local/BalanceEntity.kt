package com.example.exchangesample.data.models.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.exchangesample.data.local.BigDecimalConverter
import java.math.BigDecimal


@Entity(tableName = "balances")
@TypeConverters(BigDecimalConverter::class)
data class BalanceEntity(
    @PrimaryKey
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "balance") val balance: BigDecimal
)

