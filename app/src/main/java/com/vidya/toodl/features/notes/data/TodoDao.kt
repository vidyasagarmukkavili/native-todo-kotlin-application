package com.vidya.toodl.features.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    // Inserts an item into the database and replaces one already exists
    @Upsert
    suspend fun insertToDo(toDoItem: Todo)

    @Delete
    suspend fun deleteTodoItem(toDoItem: Todo)

    @Query("SELECT * FROM todo_list_entity ORDER BY id")
    fun getAllToDoItems(): Flow<List<Todo>>

    @Query("UPDATE todo_list_entity SET status = :status WHERE id= :id")
    suspend fun updateStatusById(status: Boolean, id: Int)
}
