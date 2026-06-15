package com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.resolum.intiva.core.data.local.room.BaseDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao : BaseDao<CategoryEntity> {

    @Query("""
        SELECT * FROM categories
        WHERE ownerType = :ownerType
        AND ownerId = :ownerId
        AND type = :type
        AND isActive = 1
        ORDER BY name ASC
    """)
    fun observeActiveCategories(
        ownerType: String,
        ownerId: Long,
        type: String
    ): Flow<List<CategoryEntity>>

    @Query("""
        SELECT * FROM categories
        WHERE ownerType = :ownerType
        AND ownerId = :ownerId
        AND type = :type
        AND isActive = 1
        ORDER BY name ASC
    """)
    suspend fun getActiveCategories(
        ownerType: String,
        ownerId: Long,
        type: String
    ): List<CategoryEntity>

    @Query("DELETE FROM categories WHERE ownerType = :ownerType AND ownerId = :ownerId AND type = :type")
    suspend fun deleteByOwnerAndType(
        ownerType: String,
        ownerId: Long,
        type: String
    )
}
