package com.example.exchangesample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.exchangesample.data.models.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT COUNT(*) FROM transactions")
    fun getCount(): Flow<Int>

    @Insert
    suspend fun insert(transactionEntity: TransactionEntity)
}