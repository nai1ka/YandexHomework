package ru.ndevelop.yandexhomework.di.modules

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.ndevelop.tinkoffproject.core.api.AuthHeaderInterceptor
import ru.ndevelop.tinkoffproject.core.api.TodoApi
import ru.ndevelop.yandexhomework.core.ApiConsts
import ru.ndevelop.yandexhomework.core.FetchDataWorkerFactory
import ru.ndevelop.yandexhomework.data.TodoItemsRepository
import ru.ndevelop.yandexhomework.data.source.local.AppDatabase
import ru.ndevelop.yandexhomework.data.source.local.LocalDataSource
import ru.ndevelop.yandexhomework.data.source.local.LocalDataSourceImpl
import ru.ndevelop.yandexhomework.data.source.remote.RemoteDataSource
import ru.ndevelop.yandexhomework.data.source.remote.RemoteDataSourceImpl
import ru.ndevelop.yandexhomework.di.ApplicationContext
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Singleton
    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: RemoteDataSourceImpl): RemoteDataSource

    @Singleton
    @Binds
    abstract fun bindLocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource

    companion object {
        @Provides
        @Singleton
        fun provideJsonSerializer(): Json = Json {
            ignoreUnknownKeys = true
        }


        @Provides
        @Singleton
        fun provideClient(authHeaderInterceptor: AuthHeaderInterceptor): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(authHeaderInterceptor).build()


        @Provides
        @Singleton
        fun provideRetrofitClient(
            client: OkHttpClient,
            jsonSerializer: Json
        ): TodoApi {
            val retrofit = Retrofit.Builder().baseUrl(ApiConsts.BASE_URL).addConverterFactory(
                jsonSerializer.asConverterFactory("application/json; charset=UTF8".toMediaType())
            ).client(client).build()

            return retrofit.create(TodoApi::class.java)
        }


        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "AppDatabase.db"
            ).build()
        }

        @Singleton
        @Provides
        fun provideTodoDao(db: AppDatabase) = db.todoDao()

        @Singleton
        @Provides
        fun provideFetchDataWorkerFactory(todoItemsRepository: TodoItemsRepository) =
            FetchDataWorkerFactory(todoItemsRepository)

        @SuppressLint("HardwareIds")
        @Singleton
        @Provides
        fun provideTodoItemsRepository(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource,
            @ApplicationContext context: Context
        ) = TodoItemsRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            deviceID = Settings.Secure.getString(
                context.contentResolver, Settings.Secure.ANDROID_ID
            )
        )
    }

}
