package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainWithCollectViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  private val _userFlow = MutableStateFlow<UiState>(UiState.Loading)
  val userFlow: StateFlow<UiState> = _userFlow.asStateFlow()

  fun onRefresh() {
    viewModelScope.launch {
      userRepository
        .getUsers().asResult()
        .collect { result ->
          _userFlow.update {
            when (result) {
              is Result.Loading -> UiState.Loading
              is Result.Success -> UiState.Success(result.data)
              is Result.Error -> UiState.Error(result.exception)
            }
          }
        }
    }
  }
}