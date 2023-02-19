package com.yasinkacmaz.jetflix.ui.settings

import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.FakeStringDataStore
import com.yasinkacmaz.jetflix.util.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class LanguageDataStoreTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val fakeStringDataStore = FakeStringDataStore()

    @Test
    fun `Should return default language when preference is not present`() = runTest {
        fakeStringDataStore.set("")

        val languageDataStore = createLanguageDataStore()

        expectThat(languageDataStore.language.first()).isEqualTo(Language.default)
    }

    @Test
    fun `Should return saved language when preference is present`() = runTest {
        val turkishLanguage = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
        fakeStringDataStore.set(turkishLanguage)

        val languageDataStore = createLanguageDataStore()

        expectThat(languageDataStore.language.first()).isEqualTo(turkishLanguage)
    }

    private fun createLanguageDataStore() = LanguageDataStore(json, fakeStringDataStore)
}
