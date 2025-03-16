package com.example.firstapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                        FloatingActionButton(onClick = {}) {
                            Icon(Icons.Filled.Add, "Floating Action Button")
                        }
                    }

                ) { contentPadding ->
                    // Observe the ViewModel's state using collectAsState()
                    val todoList = todoViewModel.todos.collectAsState().value

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = contentPadding.calculateTopPadding())
                    ) {
                        TodoList(
                            todoList = todoList,
                            todoViewModel
                        )
                        AddTodoItemTextField(todoViewModel)

                    }
                }
            }
        }
    }

    @Composable
    fun AddTodoItemTextField(todoViewModel: TodoViewModel) {
        var todoText by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            value = todoText,
            onValueChange = { todoText = it },
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
                        val newTodo = Todo(
                            id = 0,
                            status = false,
                            todoItem = todoText,
                            description = null
                        )
                        todoViewModel.addTodo(newTodo)
                        todoText = ""
                        keyboardController?.hide()
                    }
                }
            ),
            label = { Text("Add Todo Item", color = MaterialTheme.colorScheme.onBackground) },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        )
    }

    @Composable
    fun TodoList(todoList: List<Todo>, todoViewModel: TodoViewModel) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xffECDFCC))
        ) {
            items(todoList) { todo ->
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

        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(Color(0xffECDFCC))
        ) {
            val context = LocalContext.current
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    when (it) {
                        SwipeToDismissBoxValue.StartToEnd -> {
                            todoViewModel.deleteToDo(todo)
                            Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show()
                            true
                        }

                        SwipeToDismissBoxValue.EndToStart -> false

                        SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
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
                }
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
        if (dismissState.dismissDirection != SwipeToDismissBoxValue.Settled && dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.padding(start = 15.dp)
                )
                //            Spacer(modifier = Modifier.fillMaxSize())
                //            Icon(
                //                painter = painterResource(R.drawable.baseline_delete_24),
                //                contentDescription = "Swipe Right to Delete"
                //            )
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
