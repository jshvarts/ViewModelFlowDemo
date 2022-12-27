package com.jshvarts.viewmodelflowdemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ViewMainActivity : AppCompatActivity(R.layout.activity_main) {

  private val viewModel by viewModels<MainWithCollectViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.userList.collect {
          render(it)
        }
      }
    }

    viewModel.onRefresh()
  }

  private fun render(uiState: UiState) {
    when (uiState) {
      UiState.Loading -> {
        Timber.d("Loading")
      }
      is UiState.Success -> {
        Timber.d("Success ${uiState.data}")
      }
      is UiState.Error -> {
        Timber.d("Error ${uiState.throwable}")
      }
    }
  }
}