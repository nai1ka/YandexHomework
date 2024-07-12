package ru.ndevelop.yandexhomework.di.components

import dagger.Component
import ru.ndevelop.yandexhomework.di.AddItemFragmentScope
import ru.ndevelop.yandexhomework.presentation.screens.addItem.AddItemFragment

@AddItemFragmentScope
@Component(dependencies = [AppComponent::class])
interface AddItemComponent {
    fun inject(addItemFragment: AddItemFragment)

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): AddItemComponent
    }
}
