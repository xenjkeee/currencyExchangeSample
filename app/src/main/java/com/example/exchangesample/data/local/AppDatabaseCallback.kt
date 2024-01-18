package com.example.exchangesample.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.exchangesample.data.local.dao.BalanceDao
import com.example.exchangesample.data.models.local.BalanceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Provider

class AppDatabaseCallback @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val balanceDao: Provider<BalanceDao>
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        coroutineScope.launch {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() = balanceDao.get().setBalance(
        BalanceEntity("EUR", BigDecimal(1000))
    )

}