package com.vidya.toodl.features.notes.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vidya.toodl.features.notes.data.Todo
import com.vidya.toodl.features.notes.data.TodoDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val todoDao: TodoDao) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

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
            todoDao.getAllToDoItems().collect { todoList ->
                _todos.value = todoList
            }
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.insertToDo(todo)
        }
    }

    fun updateTodoStatus(isChecked: Boolean, todo: Todo) {
        viewModelScope.launch {
            todoDao.updateStatusById(status = isChecked, id = todo.id)
        }
    }

    fun deleteToDo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodoItem(todo)
        }
    }

}