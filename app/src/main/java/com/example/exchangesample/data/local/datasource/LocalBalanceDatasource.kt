package com.example.exchangesample.data.local.datasource

import com.example.exchangesample.data.local.dao.BalanceDao
import com.example.exchangesample.data.models.local.BalanceEntity
import javax.inject.Inject

class LocalBalanceDatasource @Inject constructor(
    private val balanceDao: BalanceDao,
) {
    fun getBalances() = balanceDao.getBalances()
    suspend fun setBalance(balance: BalanceEntity) = balanceDao.setBalance(balance)
}