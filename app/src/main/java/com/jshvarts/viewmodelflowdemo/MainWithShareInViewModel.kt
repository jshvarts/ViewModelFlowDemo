package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

private const val DEFAULT_TIMEOUT = 5000L

@HiltViewModel
class MainWithShareInViewModel @Inject constructor(
  userRepository: UserRepository
) : ViewModel() {

  val userFlow: SharedFlow<UiState> = userRepository
    .getUsers()
    .asResult()
    .map { result ->
      when (result) {
        is Result.Loading -> UiState.Loading
        is Result.Success -> UiState.Success(result.data)
        is Result.Error -> UiState.Error(result.exception)
      }
    }
    .shareIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )
}