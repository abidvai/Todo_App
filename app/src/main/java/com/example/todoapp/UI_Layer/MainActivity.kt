package com.example.todoapp.UI_Layer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.todoapp.Repository.TodoRepository
import com.example.todoapp.Data_Layer.TodoDatabase
import com.example.todoapp.ui.theme.TodoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(applicationContext,
            TodoDatabase::class.java,
            "todo_database").build()
        val repository = TodoRepository(database.todoDao())
        val viewModel: TodoViewModel by viewModels {
            TodoViewModelFactory(repository)
        }

        enableEdgeToEdge()
        setContent {
            TodoAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodoListPage(viewModel)
                }
            }
        }
    }
}