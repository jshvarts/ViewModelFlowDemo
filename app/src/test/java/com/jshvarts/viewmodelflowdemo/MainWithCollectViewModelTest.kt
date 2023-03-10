package com.jshvarts.viewmodelflowdemo

import app.cash.turbine.test
import com.jshvarts.viewmodelflowdemo.data.TestUserRepository
import com.jshvarts.viewmodelflowdemo.data.User
import com.jshvarts.viewmodelflowdemo.util.MainDispatcherRule
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainWithCollectViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val repository = TestUserRepository()

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `when initialized, repository emits loading and data`() = runTest {
    val viewModel = MainWithCollectViewModel(repository)

    val users = listOf(
      User(
        name = "User 1", age = 20
      ), User(
        name = "User 2", age = 30
      )
    )

    assertEquals(UiState.Loading, viewModel.userFlow.value)

    repository.sendUsers(users)

    viewModel.onRefresh()

    assertEquals(UiState.Success(users), viewModel.userFlow.value)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  fun `when initialized, repository emits loading and data (using turbine)`() = runTest {
    val viewModel = MainWithCollectViewModel(repository)

    val users = listOf(
      User(
        name = "User 1", age = 20
      ), User(
        name = "User 2", age = 30
      )
    )

    viewModel.userFlow.test {
      val firstItem = awaitItem()
      assertEquals(UiState.Loading, firstItem)

      repository.sendUsers(users)

      viewModel.onRefresh()

      val secondItem = awaitItem()
      assertEquals(UiState.Success(users), secondItem)
    }
  }
}