package com.yasinkacmaz.playground.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    override fun onCleared() {
        super.onCleared()
    }
}
