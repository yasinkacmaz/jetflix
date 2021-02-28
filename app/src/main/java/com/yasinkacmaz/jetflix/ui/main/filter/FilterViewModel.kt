package com.yasinkacmaz.jetflix.ui.main.filter

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FilterViewModel @ViewModelInject constructor(
    private val filterDataStore: FilterDataStore
) : ViewModel() {
    val filterState: StateFlow<FilterState> by lazy {
        runBlocking {
            filterDataStore.filterState.stateIn(viewModelScope)
        }
    }

    fun onResetClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            filterDataStore.resetFilterState()
        }
    }

    fun onFilterStateChanged(filterState: FilterState) {
        viewModelScope.launch(Dispatchers.IO) {
            filterDataStore.onFilterStateChanged(filterState)
        }
    }
}
