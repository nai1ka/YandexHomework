package ru.ndevelop.yandexhomework.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ndevelop.yandexhomework.data.models.local.LocalTodo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(todos: List<LocalTodo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: LocalTodo)

    @Query("SELECT * FROM todos")
    fun getFlowOfAll(): Flow<List<LocalTodo>>

    @Query("SELECT * FROM todos")
    suspend fun getAll(): List<LocalTodo>

    @Query("DELETE FROM todos WHERE id=:id")
    suspend fun delete(id: String)



}