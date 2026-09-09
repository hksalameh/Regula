package com.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today

enum class BottomTab(val index: Int, val titleRes: String, val icon: androidx.compose.ui.graphics.ImageVector) {
    TODAY(0, "today", Today),
    HISTORY(1, "history", History),
    REPORT(2, "report", Description),
    SETTINGS(4, "settings", Settings);

    companion object {
        fun valuesList(): List<BottomTab> = values().sortedBy { it.index }
    }
}