package ru.ndevelop.yandexhomework.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ndevelop.yandexhomework.data.models.local.LocalTodo

@Database(
    entities = [LocalTodo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}