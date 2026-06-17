package com.resolum.intiva.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.resolum.intiva.features.finances.data.local.dao.PendingTransactionDao
import com.resolum.intiva.features.finances.data.local.dao.TransactionDao
import com.resolum.intiva.features.finances.data.local.entities.PendingTransactionEntity
import com.resolum.intiva.features.finances.data.local.entities.TransactionWithDesignEntity
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao.CategoryDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao.FinancialAccountDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.CategoryEntity
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.FinancialAccountEntity

/**
 * Base Room database for the application.
 *
 * Add your [@Entity] classes to the entities array and increase version
 * when the schema changes.
 *
 * Example:
 * ```
 * @Database(entities = [UserEntity::class, ProductEntity::class], version = 1)
 * abstract class AppDatabase : BaseAppDatabase() {
 *     abstract fun userDao(): UserDao
 * }
 * ```
 *
 * Provided as a singleton via Hilt — see [com.resolum.intiva.core.di.DatabaseModule].
 */
@Database(
    entities = [
        PendingTransactionEntity::class,
        TransactionWithDesignEntity::class,
        CategoryEntity::class,
        FinancialAccountEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pendingTransactionDao(): PendingTransactionDao

    abstract fun transactionDao(): TransactionDao

    abstract fun categoryDao(): CategoryDao

    abstract fun financialAccountDao(): FinancialAccountDao
}
