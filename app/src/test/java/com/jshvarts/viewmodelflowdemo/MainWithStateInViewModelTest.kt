package com.jshvarts.viewmodelflowdemo

import app.cash.turbine.test
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

class MainWithStateInViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val repository = TestUserRepository()

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `when initialized, repository emits loading and data`() = runTest {
    val viewModel = MainWithStateInViewModel(repository)

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
      viewModel.userFlow.collect()
    }

    assertEquals(UiState.Loading, viewModel.userFlow.value)

    repository.sendUsers(users)
    assertEquals(UiState.Success(users), viewModel.userFlow.value)

    collectJob.cancel()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `when initialized, repository emits loading and data (using turbine)`() = runTest {
    val viewModel = MainWithShareInViewModel(repository)

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

    repository.sendUsers(users)

    viewModel.userFlow.test {
      val firstItem = awaitItem()
      assertEquals(UiState.Loading, firstItem)

      val secondItem = awaitItem()
      assertEquals(UiState.Success(users), secondItem)
    }
  }
}