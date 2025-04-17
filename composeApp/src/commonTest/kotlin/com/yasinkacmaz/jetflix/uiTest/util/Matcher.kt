package com.yasinkacmaz.jetflix.uiTest.util

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

fun withRole(role: Role) = SemanticsMatcher("${SemanticsProperties.Role.name} contains '$role'") {
    val roleProperty = it.config.getOrNull(SemanticsProperties.Role) ?: false
    roleProperty == role
}

fun withStringResource(resource: StringResource): String {
    return runBlocking { getString(resource) }
}
