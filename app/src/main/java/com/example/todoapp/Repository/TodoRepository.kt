package com.example.todoapp.Repository

import com.example.todoapp.Data_Layer.Todo
import com.example.todoapp.Data_Layer.TodoDao
import kotlinx.coroutines.flow.Flow

class TodoRepository(private val tododao: TodoDao) {

    val getAllContent: Flow<List<Todo>> = tododao.getAllTodo()

    suspend fun insert(todo: Todo){
        tododao.insert(todo)
    }
    suspend fun delete(todo: Todo){
        tododao.delete(todo)
    }
    suspend fun update(todo: Todo){
        tododao.update(todo)
    }

    fun searchResult(searchText: String): Flow<List<Todo>> {
        return tododao.searchResult(searchText)
    }

}