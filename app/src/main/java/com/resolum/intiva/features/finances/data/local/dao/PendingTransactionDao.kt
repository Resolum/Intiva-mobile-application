package com.resolum.intiva.features.finances.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.resolum.intiva.features.finances.data.local.entities.PendingTransactionEntity
import com.resolum.intiva.features.finances.domain.models.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: PendingTransactionEntity): Long

    @Query("SELECT * FROM pending_transactions WHERE syncStatus = :status ORDER BY date ASC")
    suspend fun getByStatus(status: SyncStatus): List<PendingTransactionEntity>

    @Query("SELECT * FROM pending_transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): PendingTransactionEntity?

    @Query("UPDATE pending_transactions SET syncStatus = :status, conflictReason = NULL WHERE id = :id")
    suspend fun updateStatus(id: Long, status: SyncStatus)

    @Query("UPDATE pending_transactions SET syncStatus = :status, conflictReason = :reason WHERE id = :id")
    suspend fun markFailed(id: Long, status: SyncStatus = SyncStatus.FAILED, reason: String)

    @Query("SELECT COUNT(*) FROM pending_transactions WHERE syncStatus = :status")
    fun observeCountByStatus(status: SyncStatus): Flow<Int>

    @Query("SELECT conflictReason FROM pending_transactions WHERE syncStatus = :status AND conflictReason IS NOT NULL ORDER BY date DESC LIMIT 1")
    fun observeLatestConflictReason(status: SyncStatus = SyncStatus.FAILED): Flow<String?>
}
