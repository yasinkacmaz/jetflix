package com.yasinkacmaz.playground

import com.yasinkacmaz.playground.viewmodel.BaseViewModel
import retrofit2.Retrofit
import javax.inject.Inject

class MainViewModel @Inject constructor(private val retrofit: Retrofit) : BaseViewModel() {

}
