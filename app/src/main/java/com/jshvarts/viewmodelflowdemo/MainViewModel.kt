package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.User
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val DEFAULT_TIMEOUT = 5000L

sealed class UiState {
  object Loading : UiState()

  data class Success(
    val data: List<User>
  ) : UiState()

  data class Error(
    val throwable: Throwable
  ) : UiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
  userRepository: UserRepository
) : ViewModel() {

  val userList: StateFlow<UiState> = userRepository
    .getUsers()
    .map { UiState.Success(it) }
    .catch {
      UiState.Error(it)
    }
    .onCompletion {
      println("jls flow completed")
    }
    .stateIn(
      scope = viewModelScope,
      initialValue = UiState.Loading,
      started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )
}