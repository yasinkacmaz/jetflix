package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

class ThemeDataStoreTest {

    private val fakeStringDataStore = FakeStringDataStore()

    @Test
    fun `Should return SYSTEM_DEFAULT when preference is not present`() = runTest {
        val themeDataStore = createThemeDataStore()

        themeDataStore.themePreference.first() shouldBe ThemePreference.SYSTEM_DEFAULT
    }

    @Test
    fun `Should return saved theme preference when preference is present`() = runTest {
        val themeDataStore = createThemeDataStore()

        themeDataStore.setThemePreference(ThemePreference.DARK)

        themeDataStore.themePreference.first() shouldBe ThemePreference.DARK
    }

    private fun createThemeDataStore() = ThemeDataStore(fakeStringDataStore)
}
