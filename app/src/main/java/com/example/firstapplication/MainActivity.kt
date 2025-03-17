package com.example.firstapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.firstapplication.data.AppDatabase
import com.example.firstapplication.data.Todo
import com.example.firstapplication.ui.theme.FirstApplicationTheme
import com.example.firstapplication.viewmodel.TodoViewModel
import com.example.firstapplication.viewmodel.TodoViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the database instance
        val db = AppDatabase.getDatabase(this)
        val todoDao = db.todoDao()

        val todoViewModel: TodoViewModel by viewModels {
            TodoViewModelFactory(todoDao)
        }

        enableEdgeToEdge()
        setContent {
//            var showTodoDialogBox by remember { mutableStateOf(false) }

//            var showTodoDialogBox = todoViewModel.showTodoDialogBox

            val showTodoDialogBox = todoViewModel.showTodoDialogBox

            FirstApplicationTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        Row {
                            Header(
                                heading = "To do List 📝"
                            )

                        }
                    },
                    floatingActionButton = {

                        FloatingActionButton(
                            onClick = {
                                todoViewModel.showTodoDialogBox = true
                            }

                        ) {
                            Icon(Icons.Filled.Add, "Floating Action Button")
                        }
                    }

                ) { contentPadding ->
                    // Observe the ViewModel's state using collectAsState()
                    val todoList = todoViewModel.todos.collectAsState().value

                    Box() {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = contentPadding.calculateTopPadding())
                        ) {
                            TodoList(
                                todoList = todoList,
                                todoViewModel
                            )
                        }
                        if (todoViewModel.showTodoDialogBox) {
                            AddTodoItemDialogBox(todoViewModel)
                        }
//                        AddTodoItemTextField(todoViewModel)

                    }
                }
            }
        }
    }

    @Composable
    fun AddTodoItemDialogBox(
        todoViewModel: TodoViewModel
    ) {
        val openDialog = remember { mutableStateOf(true) }

        Dialog(onDismissRequest = { openDialog.value = false }) {
            Card(
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(size = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Add a new Todo Item 📝", modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent),
                        textAlign = TextAlign.Center
                    )
                    AddTodoItemTextField(todoViewModel)
                }
            }
        }
    }

    @Composable
    fun AddTodoItemTextField(todoViewModel: TodoViewModel) {
        var todoText by remember { mutableStateOf("") }
        var todoDesc by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier.focusable() // Ensures focus can shift away from the TextField
            ,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = todoText,
                onValueChange = { todoText = it },
                placeholder = { Text(text = "Todo Item Title") },
                maxLines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (todoText.isNotEmpty()) {
//                            val newTodo = Todo(
//                                id = 0,
//                                status = false,
//                                todoItem = todoText,
//                                description = null
//                            )
//                            todoViewModel.addTodo(newTodo)
//                            todoText = ""
//                            keyboardController?.hide()
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                ),
                label = { Text("Add Todo Item", color = MaterialTheme.colorScheme.onBackground) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
            OutlinedTextField(
                value = todoDesc,
                onValueChange = { todoDesc = it },
                placeholder = { Text(text = "Todo Item Description") },
                maxLines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (todoDesc.isNotEmpty()) {
//                            val newTodo = Todo(
//                                id = 0,
//                                status = false,
//                                todoItem = todoText,
//                                description = todoDesc
//                            )
//                            todoViewModel.addTodo(newTodo)
//                            todoText = ""
//                            todoDesc = ""
//                            keyboardController?.hide()
//                            focusManager.clearFocus()
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                ),
                label = {
                    Text(
                        "Add a Description",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        todoViewModel.showTodoDialogBox = false
                        todoDesc = ""
                        todoText = ""
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    modifier = Modifier.padding(end = 10.dp),
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) { Text(text = "Close") }
                Button(
                    onClick = {
                        if (todoDesc.isNotEmpty() && todoText.isNotEmpty()) {
                            val newTodo = Todo(
                                id = 0,
                                status = false,
                                todoItem = todoText,
                                description = todoDesc
                            )
                            todoViewModel.showTodoDialogBox = false
                            todoViewModel.addTodo(newTodo)
                            todoDesc = ""
                            todoText = ""
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    )
                ) { Text(text = "Submit") }
            }
        }
    }


    @Composable
    fun TodoList(todoList: List<Todo>, todoViewModel: TodoViewModel) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xffECDFCC))
                .animateContentSize()

        ) {
            items(todoList, key = { it.id }) { todo ->
                TodoItem(todo = todo, todoViewModel)
            }
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TodoItem(
        todo: Todo,
        todoViewModel: TodoViewModel
    ) {
        val standardPadding = 30.dp

        var todoStatus by remember { mutableStateOf(false) }

        if (todo.status.equals(false)) {
            todoStatus = false
        } else if (todo.status.equals(true)) {
            todoStatus = true
        }

        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xffECDFCC))
        ) {
            val context = LocalContext.current
            val haptic = LocalHapticFeedback.current

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { targetState ->
                    when (targetState) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
                            todoViewModel.deleteToDo(todo)
                            Toast.makeText(
                                context,
                                "Todo Deleted! \uD83D\uDDD1\uFE0F",
                                Toast.LENGTH_SHORT
                            ).show()
                            true
                        }

                        SwipeToDismissBoxValue.EndToStart -> false
                        SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
                        else -> false
                    }
                },
                initialValue = SwipeToDismissBoxValue.Settled,
                positionalThreshold = { it * 0.5F }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = { DismissBackground(dismissState) },
                content = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 5.dp, start = standardPadding)
                            .fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = todoStatus,
                            onCheckedChange = {
                                todoViewModel.updateTodoStatus(it, todo)
                            }
                        )
                        Text(
                            text = todo.todoItem,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            textDecoration = if (todoStatus) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                },
                enableDismissFromEndToStart = false
            )
        }
    }

    @Composable
    fun DismissBackground(dismissState: SwipeToDismissBoxState) {
        val color = when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
            SwipeToDismissBoxValue.EndToStart -> Color.Transparent
            SwipeToDismissBoxValue.Settled -> Color.Transparent
        }
        if (
            dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd &&
            dismissState.dismissDirection != SwipeToDismissBoxValue.Settled
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(dismissState.progress)
                    .fillMaxHeight()
                    .background(color),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.padding(start = 15.dp)
                )

            }

        }
    }

    @Composable
    fun Header(
        heading: String,
        modifier: Modifier = Modifier,
    ) {
        val standardPadding = 30.dp

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)

        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xff3C3D37))
                .padding(top = 20.dp, start = standardPadding, end = standardPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 10.dp)
            ) {
                Text(
                    text = heading,
                    color = Color(0xFFECDFCC),
                    fontSize = 30.sp
                )
                Text(
                    text = formattedDate,
                    color = Color(0xFFECDFCC),
                    fontSize = 15.sp
                )

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FirstApplicationTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier =
                    Modifier
                        .fillMaxHeight()
                        .background(Color(0xff1E201E)),
                ) {
                    Header(
                        heading = "Todo Preview 📝",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}
