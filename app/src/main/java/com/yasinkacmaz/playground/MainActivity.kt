package com.yasinkacmaz.playground

import android.os.Bundle
import androidx.ui.core.Text
import androidx.ui.core.setContent
import com.yasinkacmaz.playground.activity.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        print(mainViewModel)
        setContent {
            PlaygroundTheme {
                Text("Hello World")
            }
        }
    }
}
