package com.yasinkacmaz.playground.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

abstract class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
