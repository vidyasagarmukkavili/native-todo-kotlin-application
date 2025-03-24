package com.vidya.toodl.features.notes.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    entities = [Task::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TaskDao
}
