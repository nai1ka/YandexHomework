package ru.ndevelop.yandexhomework.di.components

import dagger.Component
import ru.ndevelop.yandexhomework.di.ItemListFragmentScope
import ru.ndevelop.yandexhomework.presentation.screens.itemList.ItemListFragment

@ItemListFragmentScope
@Component(dependencies = [AppComponent::class])
interface ItemListComponent {
    fun inject(itemListFragment: ItemListFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): ItemListComponent
    }

}