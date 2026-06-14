package com.resolum.intiva.features.finances.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.resolum.intiva.core.data.local.room.BaseDao
import com.resolum.intiva.features.finances.data.local.entities.TransactionWithDesignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao : BaseDao<TransactionWithDesignEntity> {

    @Query("""
        SELECT * FROM transactions
        WHERE ownerId = :ownerId
        AND (:transactionType IS NULL OR transactionType = :transactionType)
        ORDER BY registeredAt DESC
    """)
    suspend fun getTransactions(
        ownerId: Long,
        transactionType: String?
    ): List<TransactionWithDesignEntity>

    @Query("""
        SELECT * FROM transactions
        WHERE ownerId = :ownerId
        AND (:transactionType IS NULL OR transactionType = :transactionType)
        ORDER BY registeredAt DESC
    """)
    fun observeTransactions(
        ownerId: Long,
        transactionType: String?
    ): Flow<List<TransactionWithDesignEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE ownerId = :ownerId
        ORDER BY registeredAt DESC
        LIMIT :limit
    """)
    suspend fun getLatestTransactions(
        ownerId: Long,
        limit: Int
    ): List<TransactionWithDesignEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TransactionWithDesignEntity?

    @Query("""
        DELETE FROM transactions
        WHERE ownerId = :ownerId
        AND (:transactionType IS NULL OR transactionType = :transactionType)
    """)
    suspend fun deleteByOwnerAndType(
        ownerId: Long,
        transactionType: String?
    )
}
