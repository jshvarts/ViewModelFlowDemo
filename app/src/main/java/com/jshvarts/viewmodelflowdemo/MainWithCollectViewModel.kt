package com.jshvarts.viewmodelflowdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainWithCollectViewModel @Inject constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  private val _userList = MutableStateFlow(UiState.Loading as UiState)
  val userList: StateFlow<UiState> = _userList

  fun onRefresh() {
    viewModelScope.launch {
      userRepository
        .getUsers().asResult()
        .collect { result ->
          _userList.value = when (result) {
            is Result.Loading -> UiState.Loading
            is Result.Success -> UiState.Success(result.data)
            is Result.Error -> UiState.Error(result.exception)
          }
        }
    }
  }
}