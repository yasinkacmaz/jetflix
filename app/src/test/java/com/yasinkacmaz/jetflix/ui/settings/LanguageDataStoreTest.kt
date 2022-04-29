package com.yasinkacmaz.jetflix.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.yasinkacmaz.jetflix.ui.settings.LanguageDataStore.Companion.KEY_LANGUAGE
import com.yasinkacmaz.jetflix.util.CoroutineTestRule
import com.yasinkacmaz.jetflix.util.json
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.serialization.encodeToString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class LanguageDataStoreTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    private lateinit var preferences: DataStore<Preferences>

    private lateinit var languageDataStore: LanguageDataStore

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        languageDataStore = LanguageDataStore(json, preferences)
    }

    @Test
    fun `Should return default language when preference is not present`() {
        coroutineTestRule.runBlockingTest {
            val prefs = mockk<Preferences>()
            coEvery { preferences.data } returns flowOf(prefs)

            languageDataStore.language.collect { language ->
                val defaultLanguage = Language.default
                expectThat(language).isEqualTo(defaultLanguage)
            }
        }
    }

    @Test
    fun `Should return saved language when preference is present`() {
        coroutineTestRule.runBlockingTest {
            val prefs = mockk<Preferences>()
            val turkishLanguage = Language(englishName = "Turkish", iso6391 = "tr", name = "Türkçe")
            coEvery { preferences.data } returns flowOf(prefs)
            every { prefs[KEY_LANGUAGE] } returns json.encodeToString(turkishLanguage)

            languageDataStore.language.collect { language ->
                expectThat(language).isEqualTo(turkishLanguage)
            }
        }
    }
}
