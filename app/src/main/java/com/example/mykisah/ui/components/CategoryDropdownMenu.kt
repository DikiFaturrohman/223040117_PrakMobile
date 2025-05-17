package com.example.mykisah.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdownMenu(
    categories: List<String>,
    selected: String,
    allowCustom: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var isCustom by remember { mutableStateOf(false) }
    var customValue by remember { mutableStateOf("") }

    // Check if selected value is in categories list
    LaunchedEffect(selected) {
        if (selected.isNotEmpty() && !categories.contains(selected) && allowCustom) {
            isCustom = true
            customValue = selected
        }
    }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = if (isCustom) customValue else selected,
                onValueChange = {
                    if (isCustom) {
                        customValue = it
                        onValueChange(it)
                    }
                },
                readOnly = !isCustom,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                label = { Text("Pilih Kategori") }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                            isCustom = false
                        }
                    )
                }

                if (allowCustom) {
                    DropdownMenuItem(
                        text = { Text("Kategori Kustom...") },
                        onClick = {
                            isCustom = true
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}