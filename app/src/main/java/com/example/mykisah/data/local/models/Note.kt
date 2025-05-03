package com.example.mykisah.data.local.models

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String

)
