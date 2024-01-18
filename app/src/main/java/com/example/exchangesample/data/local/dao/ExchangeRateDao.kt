package com.example.exchangesample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.exchangesample.data.models.local.ExchangeRateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeRateDao {

    @Query("SELECT * FROM exchange_rates WHERE from_currency == :fromCurrency ORDER BY to_currency")
    fun getExchangeRates(fromCurrency: String): Flow<List<ExchangeRateEntity>>

    @Transaction
    suspend fun replaceAll(rates: List<ExchangeRateEntity>) {
        deleteAll()
        insertAll(rates)
    }


    @Query("DELETE FROM exchange_rates")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<ExchangeRateEntity>)

}