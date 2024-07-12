package com.example.todoapp.UI_Layer

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.Data_Layer.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListPage(viewModel: TodoViewModel) {

    val context = LocalContext.current.applicationContext
    var searchText by rememberSaveable { mutableStateOf("") }
    var addBottomSheetState by rememberSaveable { mutableStateOf(false) }
    var sheetState = rememberModalBottomSheetState()
    var title by rememberSaveable { mutableStateOf("") }
    var todo by rememberSaveable { mutableStateOf("") }
    var selectedTodo by rememberSaveable { mutableStateOf<Todo?>(null) }
    val focusManager = LocalFocusManager.current
    var focusRequester = remember { FocusRequester() }

    //    var Date by remember {
//        mutableStateOf("")
//    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Todo App",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 20.dp)
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                titleContentColor = MaterialTheme.colorScheme.scrim

            )
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { addBottomSheetState = !addBottomSheetState },
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = Color.Black
        ) {
            IconButton(onClick = {
                selectedTodo = null
                title = ""
                todo = ""
                addBottomSheetState = true
            }) {
                Icon(Icons.Outlined.Add, contentDescription = "Add")
            }
        }

    }, modifier = Modifier.pointerInput(Unit) {
        detectTapGestures {
            focusManager.clearFocus()
        }
    }) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.White)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.searchTodo(it)
                    },
                    label = {
                        Text(text = "Search")
                    },
                    placeholder = {
                        Text(text = "Search Your todo")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = Color.Black
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedTextColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        .focusRequester(focusRequester)

                )
            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "********    All Your Todo    ********",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold

            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.Black, thickness = 2.dp)
            Spacer(modifier = Modifier.height(20.dp))

            val todos = viewModel.allTodos.observeAsState(initial = emptyList())

            LazyColumn {
                items(todos.value.size) { index ->
                    TodoItems(todos.value[index], onClick = {
                        selectedTodo = todos.value[index]
                        title = selectedTodo?.title ?: "" // Prepopulate title
                        todo = selectedTodo?.todo ?: "" // Prepopulate todo
                        addBottomSheetState = true // Open bottom sheet for editing
                    }, onDelete = {
                        viewModel.Delete(it)
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show()
                    })
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(
                        color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        if (addBottomSheetState) {
            ModalBottomSheet(
                onDismissRequest = { addBottomSheetState = false },
                sheetState = sheetState,
                tonalElevation = 10.dp,
                shape = RoundedCornerShape(10.dp),
                scrimColor = Color.Transparent,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = Color.Black,
                sheetMaxWidth = 500.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Todo", textAlign = TextAlign.Center, fontSize = 24.sp
                    )
                    Divider(
                        color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },
                        label = {
                            Text(text = "Title")
                        },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = todo,
                        onValueChange = {
                            todo = it
                        },
                        label = {
                            Text(text = "Todo")
                        },
                        minLines = 7,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            if (selectedTodo != null) {
                                selectedTodo?.let {
                                    viewModel.Update(it.copy(title = title, todo = todo))
                                    Toast.makeText(
                                        context, "Todo Updated Successful", Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                viewModel.AddTodo(title, todo)
                                Toast.makeText(
                                    context, "Todo Save Successful", Toast.LENGTH_LONG
                                ).show()
                            }
                            addBottomSheetState = false
                        },
                        border = BorderStroke(1.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White

                        ),
                    ) {
                        Text(
                            text = "Save Todo",
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}
