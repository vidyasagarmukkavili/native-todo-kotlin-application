package com.example.firstapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list_entity")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "status")
    val status: Boolean = false,
    @ColumnInfo(name = "todo_item")
    val todoItem: String,
    @ColumnInfo(name = "description")
    val description: String?,
)
