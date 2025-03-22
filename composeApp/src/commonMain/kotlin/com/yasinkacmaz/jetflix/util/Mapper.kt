package com.yasinkacmaz.jetflix.util

interface Mapper<Input, Output> {
    fun map(input: Input): Output
}
