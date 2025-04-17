package com.yasinkacmaz.jetflix.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasinkacmaz.jetflix.data.service.PersonService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val personId: Int,
    private val personService: PersonService,
    private val profileMapper: ProfileMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile(personId)
    }

    private fun fetchProfile(personId: Int) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(loading = true, error = null)
        _uiState.value = try {
            val profileResponse = personService.profile(personId)
            val profile = profileMapper.map(profileResponse)
            _uiState.value.copy(profile = profile, loading = false)
        } catch (exception: Exception) {
            _uiState.value.copy(error = exception, loading = false)
        }
    }

    data class ProfileUiState(val profile: Profile? = null, val loading: Boolean = false, val error: Throwable? = null)
}
