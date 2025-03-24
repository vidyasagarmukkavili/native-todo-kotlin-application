package com.vidya.toodl.core.di

import android.content.Context
import androidx.room.Room
import com.vidya.toodl.features.notes.data.AppDatabase
import com.vidya.toodl.features.notes.data.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TaskViewModelModule {

    @Provides
    @Singleton
    fun providesAppDatabase (@ApplicationContext context: Context) : AppDatabase {

        val databaseName = "toodl"
        val applicationContext = context.applicationContext

       return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, databaseName
        ).build()
    }

    @Provides
    @Singleton
    fun providesTodoDao (db: AppDatabase) : TaskDao {
        return db.todoDao()
    }
}