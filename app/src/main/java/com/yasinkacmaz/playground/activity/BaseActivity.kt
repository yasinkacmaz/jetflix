package com.yasinkacmaz.playground.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import dagger.android.AndroidInjection

abstract class BaseActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
