package com.yasinkacmaz.jetflix.util

import io.mockk.mockk

inline fun <reified T : Any> mockkRelaxed() = mockk<T>(relaxed = true)
