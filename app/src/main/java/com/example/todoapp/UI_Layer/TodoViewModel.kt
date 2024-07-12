package com.example.todoapp.UI_Layer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.Repository.TodoRepository
import com.example.todoapp.Data_Layer.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository): ViewModel() {

    private val _searchQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val allTodos: LiveData<List<Todo>> = _searchQuery.flatMapLatest { query ->
        if (query.isNullOrEmpty()) {
            repository.getAllContent
        } else {
            repository.searchResult(query)
        }
    }.asLiveData()

    fun searchTodo(query: String?) {
        _searchQuery.value = query
    }

    fun AddTodo(
        title: String,
        todo: String
    ){
        viewModelScope.launch {
            val addTodo = Todo(0, title, todo)
            repository.insert(addTodo)
        }
    }

    fun Update(todo: Todo){
        viewModelScope.launch {
            repository.update(todo)
        }
    }

    fun Delete(todo: Todo){
        viewModelScope.launch {
            repository.delete(todo)
        }
    }
}
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}