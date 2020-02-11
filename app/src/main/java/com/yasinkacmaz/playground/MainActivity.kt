package com.yasinkacmaz.playground

import android.os.Bundle
import androidx.ui.core.Text
import androidx.ui.core.setContent
import com.yasinkacmaz.playground.activity.BaseActivity

class MainActivity : BaseActivity() {

    //private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //print(mainViewModel)
        setContent {
            Text("Hello World")
        }
    }
}
