package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.User
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

private const val DEFAULT_TIMEOUT = 5000L

sealed interface UiState {
  object Loading : UiState

  data class Success(
    val data: List<User>
  ) : UiState

  data class Error(
    val throwable: Throwable
  ) : UiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
  userRepository: UserRepository
) : ViewModel() {

  val userList: StateFlow<UiState> = userRepository
    .getUsers()
    .catch {
      Timber.e(it, "Flow catch")
      UiState.Error(it)
    }
    .map { UiState.Success(it) }
    .onEach {
      Timber.d("Flow onEach $it")
    }
    .onCompletion {
      Timber.d("Flow onCompletion")
    }
    .stateIn(
      scope = viewModelScope,
      initialValue = UiState.Loading,
      started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )
}