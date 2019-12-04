package com.yasinkacmaz.playground

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.Text
import androidx.ui.core.setContent
import com.yasinkacmaz.playground.activity.BaseActivity
import com.yasinkacmaz.playground.extension.get
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mainViewModel by lazy { viewModelFactory.get<MainViewModel>(this) }

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
