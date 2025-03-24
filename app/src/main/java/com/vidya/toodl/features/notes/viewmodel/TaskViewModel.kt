package com.vidya.toodl.features.notes.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidya.toodl.features.notes.data.Task
import com.vidya.toodl.features.notes.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor (private val taskDao: TaskDao) : ViewModel() {

    private val _todos = MutableStateFlow<List<Task>>(emptyList())
    val todos: StateFlow<List<Task>> = _todos

    //    var showTodoDialogBox by mutableStateOf(false)
    private val _showTodoDialogBox = mutableStateOf(false)
    var showTodoDialogBox: Boolean
        get() = _showTodoDialogBox.value
        set(value) {
            _showTodoDialogBox.value = value
        }

    /*
    Runs during initialization of the ViewModel
     */
    init {
        fetchAllToDoItems()
    }

    fun fetchAllToDoItems() {
        viewModelScope.launch {
            taskDao.getAllToDoItems().collect { todoList ->
                _todos.value = todoList
            }
        }
    }

    fun addTodo(task: Task) {
        viewModelScope.launch {
            taskDao.insertToDo(task)
        }
    }

    fun updateTodoStatus(isChecked: Boolean, task: Task) {
        viewModelScope.launch {
            taskDao.updateStatusById(status = isChecked, id = task.id)
        }
    }

    fun deleteToDo(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTodoItem(task)
        }
    }

}