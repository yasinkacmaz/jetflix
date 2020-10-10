package com.yasinkacmaz.jetflix.util

interface Mapper<T,U> {
    fun map(input: T): U
}
