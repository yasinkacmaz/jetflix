package com.yasinkacmaz.jetflix.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.data.service.ConfigurationService
import com.yasinkacmaz.jetflix.util.Dispatchers
import com.yasinkacmaz.jetflix.util.onIO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val configurationService: ConfigurationService,
    private val languageDataStore: LanguageDataStore,
    private val dispatchers: Dispatchers,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        languageDataStore.language
            .onEach { selectedLanguage ->
                _uiState.update { it.copy(selectedLanguage = selectedLanguage) }
            }
            .launchIn(viewModelScope)

        fetchLanguages()
    }

    private fun fetchLanguages() = viewModelScope.launch {
        _uiState.value = UiState(showLoading = true)
        val languages = try {
            configurationService.fetchLanguages().sortedBy(Language::englishName)
        } catch (exception: Exception) {
            emptyList()
        }
        _uiState.value = UiState(showLoading = false, languages = languages)
    }

    fun onLanguageSelected(language: Language) {
        dispatchers.onIO {
            languageDataStore.onLanguageSelected(language)
        }
    }

    data class UiState(
        val showLoading: Boolean = false,
        val languages: List<Language> = emptyList(),
        val selectedLanguage: Language = Language.default,
    )
}
