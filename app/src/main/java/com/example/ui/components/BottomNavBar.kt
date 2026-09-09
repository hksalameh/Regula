package com.example.ui.components

import androidx.compose.foundation.layout.
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.BottomTab
import com.example.ui.MainViewModel
import com.example.ui.SheetType
import com.example.ui.UiState
import com.example.ui.theme.MyApplicationTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun BottomNavBar(
    viewModel: MainViewModel,
    uiState: UiState.MainUiState,
    contentColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    if (uiState.selectedTab == 3) return
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = contentColor,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        BottomTab.valuesList().forEach { tab ->
            NavigationBarItem(
                selected = uiState.selectedTab == tab.index,
                onClick = { viewModel.selectTab(tab.index) },
                icon = { Icon(tab.icon, contentDescription = null) },
                label = {
                    Text(
                        if (uiState.isAr) getArabicTitle(tab) else tab.titleRes,
                        fontWeight = if (uiState.selectedTab == tab.index) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                modifier = Modifier.testTag(getTestTag(tab.index))
            )
        }
    }

private fun getArabicTitle(tab: BottomTab): String = when (tab) {
    BottomTab.TODAY -> "اليوم"
    BottomTab.HISTORY -> "السجل"
    BottomTab.REPORT -> "التقرير"
    BottomTab.SETTINGS -> "الإعدادات"
}

private fun getTestTag(index: Int): String = when (index) {
    0 -> "tab_today"
    1 -> "tab_timeline"
    2 -> "tab_report"
    else -> "tab_settings"
}
