package com.example.exchangesample.data.di

import android.app.Application
import androidx.room.Room
import com.example.exchangesample.data.local.AppDatabase
import com.example.exchangesample.data.local.AppDatabaseCallback
import com.example.exchangesample.data.local.dao.BalanceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(
        application: Application,
        callback: AppDatabaseCallback
    ) = Room.databaseBuilder(
        context = application,
        klass = AppDatabase::class.java,
        name = "app_database"
    ).addCallback(callback).build()

    @Singleton
    @Provides
    fun provideExchangeRateDao(db: AppDatabase) = db.exchangeRateDao()

    @Singleton
    @Provides
    fun provideBalanceDao(db: AppDatabase) = db.balanceDao()

    @Singleton
    @Provides
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()

    @Provides
    fun provideCoroutineScope() = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun provideAppDatabaseCallback(
        coroutineScope: CoroutineScope,
        balanceDao: Provider<BalanceDao>
    ) = AppDatabaseCallback(coroutineScope, balanceDao)
}

