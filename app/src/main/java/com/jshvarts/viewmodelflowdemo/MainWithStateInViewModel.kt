package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val DEFAULT_TIMEOUT = 5000L

@HiltViewModel
class MainWithStateInViewModel @Inject constructor(
  userRepository: UserRepository
) : ViewModel() {

  val userList: StateFlow<UiState> = userRepository
    .getUsers()
    .asResult()
    .map { result ->
      when (result) {
        is Result.Loading -> UiState.Loading
        is Result.Success -> UiState.Success(result.data)
        is Result.Error -> UiState.Error(result.exception)
      }
    }
    .stateIn(
      scope = viewModelScope,
      initialValue = UiState.Loading,
      started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )
}