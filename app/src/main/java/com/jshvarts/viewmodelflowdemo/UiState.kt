package com.jshvarts.viewmodelflowdemo

import com.jshvarts.viewmodelflowdemo.data.User

sealed interface UiState {
  object Loading : UiState

  data class Success(
    val data: List<User>
  ) : UiState

  data class Error(
    val throwable: Throwable? = null
  ) : UiState
}
