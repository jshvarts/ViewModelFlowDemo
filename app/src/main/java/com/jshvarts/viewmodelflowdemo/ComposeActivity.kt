package com.jshvarts.viewmodelflowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jshvarts.viewmodelflowdemo.ui.theme.ViewModelFlowDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

  private val viewModel by viewModels<MainWithStateInViewModel>()

  @OptIn(ExperimentalLifecycleComposeApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ViewModelFlowDemoTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          val userList by viewModel.userList.collectAsStateWithLifecycle()
          MainScreen(userList = userList)
        }
      }
    }
  }
}

@Composable
fun MainScreen(
  userList: UiState,
  modifier: Modifier = Modifier
) {
  when (userList) {
    UiState.Loading -> {
      Timber.d("Loading")
      Box(
        modifier = modifier
          .fillMaxSize()
      ) {
        CircularProgressIndicator()
      }
    }
    is UiState.Success -> {
      LazyColumn {
        items(userList.data) { item ->
          Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
              .fillParentMaxWidth()
          ) {
            Text(item.name)
            Text("${item.age}")
          }
        }
      }
    }
    is UiState.Error -> {
      Box(
        modifier = modifier
          .fillMaxSize()
      ) {
        userList.throwable
      }
    }
  }
}

