package com.vidya.toodl.features.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "status")
    val status: Boolean = false,
    @ColumnInfo(name = "todo_item")
    val todoItem: String,
    @ColumnInfo(name = "description")
    val description: String?,
)
