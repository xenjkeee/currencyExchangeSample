package com.example.exchangesample.data.local.repository

import com.example.exchangesample.data.local.datasource.LocalBalanceDatasource
import com.example.exchangesample.data.mappers.toBalance
import com.example.exchangesample.data.mappers.toBalanceEntity
import com.example.exchangesample.domain.Balance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BalanceRepository @Inject constructor(
    private val localBalanceDatasource: LocalBalanceDatasource
) {

    fun getBalances() = localBalanceDatasource.getBalances().map { list ->
        list.map { it.toBalance() }
    }

    suspend fun setBalance(balance: Balance) =
        localBalanceDatasource.setBalance(balance.toBalanceEntity())

}

