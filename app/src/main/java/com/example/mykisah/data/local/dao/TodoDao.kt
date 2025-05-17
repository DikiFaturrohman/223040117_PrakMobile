package com.example.mykisah.data.local.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mykisah.data.local.models.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getTodoById(id: String): Todo?

    @Query("SELECT * FROM todos")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE isCompleted = 0")
    fun getActiveTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos WHERE isCompleted = 1")
    fun getCompletedTodos(): Flow<List<Todo>>

    @Query("SELECT * FROM todos ORDER BY priority DESC")
    fun getTodosByPriority(): Flow<List<Todo>>

    @Query("SELECT * FROM todos ORDER BY CASE WHEN deadline IS NULL THEN 1 ELSE 0 END, deadline ASC")
    fun getTodosByDeadline(): Flow<List<Todo>>
}