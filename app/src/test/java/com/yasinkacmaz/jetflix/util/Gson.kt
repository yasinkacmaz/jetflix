package com.yasinkacmaz.jetflix.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

val gson = Gson()

inline fun <reified T : Any> parseJson(fileName: String): T {
    val typeToken: TypeToken<T> = TypeToken.get(T::class.java)
    return gson.fromJson(gson.getResourceReader(fileName), typeToken.type)
}

fun Gson.getResourceReader(fileName: String): BufferedReader {
    val resource = javaClass.classLoader!!.getResourceAsStream(fileName)
    return BufferedReader(InputStreamReader(resource, Charsets.UTF_8))
}
