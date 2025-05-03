package com.example.mykisah.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mykisah.navigation.BottomNavTab
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description // ← tambahin ini ya!
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text



@Composable
fun BottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Description, contentDescription = "Catatan") },
            label = { Text("Catatan") },
            selected = selectedTab == BottomNavTab.NOTES,
            onClick = { onTabSelected(BottomNavTab.NOTES) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "To-Do") },
            label = { Text("To-Do") },
            selected = selectedTab == BottomNavTab.TODO,
            onClick = { onTabSelected(BottomNavTab.TODO) }
        )
    }
}
