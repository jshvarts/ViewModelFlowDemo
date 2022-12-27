package com.jshvarts.viewmodelflowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jshvarts.viewmodelflowdemo.ui.theme.ViewModelFlowDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

  private val viewModel by viewModels<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ViewModelFlowDemoTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          MainScreen(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun MainScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val userList by viewModel.userList.collectAsState()
  render(userList)
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
