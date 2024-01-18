package com.example.exchangesample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exchangesample.data.local.dao.BalanceDao
import com.example.exchangesample.data.local.dao.ExchangeRateDao
import com.example.exchangesample.data.local.dao.TransactionDao
import com.example.exchangesample.data.models.local.BalanceEntity
import com.example.exchangesample.data.models.local.ExchangeRateEntity
import com.example.exchangesample.data.models.local.TransactionEntity

@Database(
    entities = [
        ExchangeRateEntity::class,
        BalanceEntity::class,
        TransactionEntity::class,
    ],
    version = 1
)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun balanceDao(): BalanceDao
    abstract fun transactionDao(): TransactionDao
}