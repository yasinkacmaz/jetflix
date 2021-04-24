package com.yasinkacmaz.jetflix.ui.main.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.service.ConfigurationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val configurationService: ConfigurationService,
    private val languageDataStore: LanguageDataStore
) : ViewModel() {

    val selectedLanguage = languageDataStore.language
    val onSettingsChanged = MutableLiveData<Unit>()
    val uiState = MutableStateFlow(UiState())

    var uiValue
        get() = uiState.value
        set(value) {
            uiState.value = value
        }

    fun fetchLanguages() {
        val canFetchLanguages = uiValue.languages.isEmpty() && uiValue.showLoading.not()
        if (canFetchLanguages) {
            viewModelScope.launch {
                uiValue = uiValue.copy(showLoading = true)
                uiValue = try {
                    val languages = configurationService.fetchLanguages().sortedBy(Language::englishName)
                    uiValue.copy(showLoading = false, languages = languages)
                } catch (exception: Exception) {
                    uiValue.copy(showLoading = false)
                }
            }
        }
    }

    fun onLanguageSelected(language: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            languageDataStore.onLanguageSelected(language)
        }
        onSettingsChanged.value = Unit
    }

    data class UiState(val showLoading: Boolean = false, val languages: List<Language> = emptyList())
}
