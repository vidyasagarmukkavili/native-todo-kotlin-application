package com.vidya.toodl.features.notes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.vidya.toodl.features.notes.data.TodoDao

/**
 * Provider Factory class which is going to create and instantiate
 * the ViewModel Object for usage in Activity
 */
class TaskViewModelFactory(private val todoDao: TodoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return TaskViewModel(todoDao) as T
    }

}