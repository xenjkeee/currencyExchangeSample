package com.example.exchangesample.data.local.repository

import com.example.exchangesample.data.local.datasource.LocalTransactionDatasource
import com.example.exchangesample.data.mappers.toTransactionEntity
import com.example.exchangesample.domain.Transaction
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val localTransactionDatasource: LocalTransactionDatasource,
) {

    fun getCount() = localTransactionDatasource.getTransactionsCount()

    suspend fun insert(transaction: Transaction) =
        localTransactionDatasource.insertTransaction(transaction.toTransactionEntity())
}