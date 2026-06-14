package com.resolum.intiva.features.paymentmethodsandcategories.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.resolum.intiva.core.data.local.room.BaseDao
import com.resolum.intiva.features.paymentmethodsandcategories.data.local.entities.FinancialAccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FinancialAccountDao : BaseDao<FinancialAccountEntity> {

    @Query("""
        SELECT * FROM financial_accounts
        WHERE userId = :userId
        AND isActive = 1
        ORDER BY name ASC
    """)
    fun observeActiveAccounts(userId: Long): Flow<List<FinancialAccountEntity>>

    @Query("""
        SELECT * FROM financial_accounts
        WHERE userId = :userId
        ORDER BY name ASC
    """)
    suspend fun getAccountsByUserId(userId: Long): List<FinancialAccountEntity>

    @Query("""
        SELECT * FROM financial_accounts
        WHERE userId = :userId
        AND isActive = 1
        ORDER BY name ASC
    """)
    suspend fun getActiveAccounts(userId: Long): List<FinancialAccountEntity>

    @Query("SELECT * FROM financial_accounts WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): FinancialAccountEntity?

    @Query("DELETE FROM financial_accounts WHERE userId = :userId")
    suspend fun deleteByUserId(userId: Long)
}
