package com.resolum.intiva.features.finances.domain.models

data class SyncStatusSummary(
    val pendingCount: Int = 0,
    val failedCount: Int = 0,
    val latestConflictReason: String? = null
)
