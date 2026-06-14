package com.resolum.intiva.core.data.local.room

import androidx.room.TypeConverter
import com.resolum.intiva.features.finances.domain.models.AccountType
import com.resolum.intiva.features.finances.domain.models.SyncStatus

class RoomConverters {

    @TypeConverter
    fun fromSyncStatus(value: SyncStatus): String = value.name

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)

    @TypeConverter
    fun fromAccountType(value: AccountType): String = value.name

    @TypeConverter
    fun toAccountType(value: String): AccountType = AccountType.valueOf(value)
}
