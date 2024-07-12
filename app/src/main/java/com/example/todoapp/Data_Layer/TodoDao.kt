package com.example.todoapp.Data_Layer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(vararg todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo_list")
    fun getAllTodo(): Flow<List<Todo>>

    @Query("Select * from todo_list where title like '%' || :seachText || '%' or todo like '%' || :seachText || '%'")
    fun searchResult(seachText: String): Flow<List<Todo>>
}