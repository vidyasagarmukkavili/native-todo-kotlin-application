package com.vidya.toodl.features.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    // Inserts an item into the database and replaces one already exists
    @Upsert
    suspend fun insertToDo(toDoItem: Task)

    @Delete
    suspend fun deleteTodoItem(toDoItem: Task)

    @Query("SELECT * FROM task ORDER BY id")
    fun getAllToDoItems(): Flow<List<Task>>

    @Query("UPDATE task SET status = :status WHERE id= :id")
    suspend fun updateStatusById(status: Boolean, id: Int)
}
