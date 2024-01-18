package com.example.exchangesample.data.local.datasource

import com.example.exchangesample.data.local.dao.TransactionDao
import com.example.exchangesample.data.models.local.TransactionEntity
import javax.inject.Inject

class LocalTransactionDatasource @Inject constructor(
    private val transactionDao: TransactionDao,
) {
    suspend fun insertTransaction(transaction: TransactionEntity) =
        transactionDao.insert(transaction)

    fun getTransactionsCount() = transactionDao.getCount()
}