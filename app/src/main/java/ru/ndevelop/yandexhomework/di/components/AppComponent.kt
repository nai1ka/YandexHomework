package ru.ndevelop.yandexhomework.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ndevelop.yandexhomework.di.ApplicationContext
import ru.ndevelop.yandexhomework.di.modules.AppModule
import ru.ndevelop.yandexhomework.presentation.MainActivity
import ru.ndevelop.yandexhomework.presentation.viewmodels.AddItemViewModel
import ru.ndevelop.yandexhomework.presentation.viewmodels.ItemListViewModel
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    val addItemViewModel: AddItemViewModel
    val itemListViewModel: ItemListViewModel

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @ApplicationContext applicationContext: Context): AppComponent
    }
}