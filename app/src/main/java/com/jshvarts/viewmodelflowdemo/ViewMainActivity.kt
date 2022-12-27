package com.jshvarts.viewmodelflowdemo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewMainActivity : AppCompatActivity(R.layout.activity_main) {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    lifecycleScope.launch { 
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.userList.collect {
          render(it)
        }
      }
    }
  }

  private fun render(uiState: UiState) {
    when (uiState) {
      UiState.Loading -> {
        println("jls loading")
      }
      is UiState.Success -> {
        println("jls success ${uiState.data}")
      }
      is UiState.Error -> {
        println("jls error $uiState")
      }
    }
  }
}