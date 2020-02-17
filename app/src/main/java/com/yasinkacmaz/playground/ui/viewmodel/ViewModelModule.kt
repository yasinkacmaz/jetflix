package com.yasinkacmaz.playground.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule  {
    @Binds
    @IntoMap
    @ViewModelKey(BaseViewModel::class)
    fun bindBaseViewModel(baseViewModel: BaseViewModel): ViewModel

    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
