package com.resolum.intiva.core.di

import android.content.Context
import androidx.room.Room
import com.resolum.intiva.core.data.local.room.AppDatabase
import com.resolum.intiva.features.finances.data.local.dao.PendingTransactionDao
import com.resolum.intiva.features.finances.data.local.dao.TransactionDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao.CategoryDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao.FinancialAccountDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "intiva_database"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    @Singleton
    fun providePendingTransactionDao(
        database: AppDatabase,
    ): PendingTransactionDao = database.pendingTransactionDao()

    @Provides
    @Singleton
    fun provideTransactionDao(
        database: AppDatabase,
    ): TransactionDao = database.transactionDao()

    @Provides
    @Singleton
    fun provideCategoryDao(
        database: AppDatabase,
    ): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideFinancialAccountDao(
        database: AppDatabase,
    ): FinancialAccountDao = database.financialAccountDao()
}
