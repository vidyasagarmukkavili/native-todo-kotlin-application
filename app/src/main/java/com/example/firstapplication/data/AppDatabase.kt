package com.example.firstapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 2,
    entities = [Todo::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {

        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE todo_list_entity ADD COLUMN description varchar(500)")
            }

        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // This method creates (or returns an existing) database instance.
        fun getDatabase(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val instance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "todo_list_entity",
                        )
                        .addMigrations(migration_1_2)
                        .build()
                INSTANCE = instance
                instance
            }
    }
}
