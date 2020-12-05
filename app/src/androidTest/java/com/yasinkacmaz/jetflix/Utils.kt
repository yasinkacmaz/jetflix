package com.yasinkacmaz.jetflix

import androidx.annotation.StringRes
import androidx.test.platform.app.InstrumentationRegistry

fun getString(@StringRes resId: Int) = InstrumentationRegistry.getInstrumentation().context.getString(resId)
