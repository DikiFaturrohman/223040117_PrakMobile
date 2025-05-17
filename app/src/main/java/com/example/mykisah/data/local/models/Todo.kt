package com.example.mykisah.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val deadline: Long? = null,
    val priority: Int = 0 // 0 = Low, 1 = Medium, 2 = High
)