package com.resolum.intiva.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base Room database for the application.
 *
 * Add your [@Entity] classes to the [entities] array and increase [version]
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
 * Provided as a singleton via Hilt — see [core.di.DatabaseModule].
 */
//@Database(entities = [], version = 1, exportSchema = true)
//abstract class AppDatabase : RoomDatabase()