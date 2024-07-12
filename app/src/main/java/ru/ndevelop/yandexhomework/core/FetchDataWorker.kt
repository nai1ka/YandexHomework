package ru.ndevelop.yandexhomework.core

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import ru.ndevelop.yandexhomework.data.TodoItemsRepository

class FetchDataWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val todoItemsRepository: TodoItemsRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            todoItemsRepository.fetchData()
            return Result.success()
        } catch (cancellationException: CancellationException) {
            throw cancellationException
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}

class FetchDataWorkerFactory(
    private val todoItemsRepository: TodoItemsRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            FetchDataWorker::class.java.name -> { FetchDataWorker(appContext, workerParameters, todoItemsRepository) }
            else -> null
        }
    }
}
