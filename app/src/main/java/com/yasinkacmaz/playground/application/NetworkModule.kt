package com.yasinkacmaz.playground.application

import com.github.kittinunf.fuel.core.FuelManager
import org.koin.dsl.module

val netWorkModule = module {
    FuelManager.instance
}
