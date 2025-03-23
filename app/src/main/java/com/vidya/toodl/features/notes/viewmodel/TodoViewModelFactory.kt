package com.vidya.toodl.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.vidya.toodl.data.TodoDao

/**
 * Provider Factory class which is going to create and instantiate
 * the ViewModel Object for usage in Activity
 */
class TodoViewModelFactory(private val todoDao: TodoDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return TodoViewModel(todoDao) as T
    }

}