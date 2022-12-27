package com.jshvarts.viewmodelflowdemo

import com.jshvarts.viewmodelflowdemo.data.TestUserRepository
import com.jshvarts.viewmodelflowdemo.data.User
import com.jshvarts.viewmodelflowdemo.util.MainDispatcherRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val repository = TestUserRepository()

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `when initialized, fake repository emits loading and data`() = runTest {
    val viewModel = MainViewModel(repository)

    val users = listOf(
      User(
        name = "User 1",
        age = 20
      ),
      User(
        name = "User 2",
        age = 30
      )
    )

    val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
      viewModel.userList.collect()
    }

    assertEquals(UiState.Loading, viewModel.userList.value)

    repository.sendUsers(users)
    assertEquals(UiState.Success(users), viewModel.userList.value)

    collectJob.cancel()
  }
}