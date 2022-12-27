package com.jshvarts.viewmodelflowdemo.data

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestUserRepository : UserRepository {

  /**
   * The backing hot flow for the list of users for testing.
   */
  private val usersFlow =
    MutableSharedFlow<List<User>>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

  override fun getUsers(): Flow<List<User>> {
    return usersFlow
  }

  /**
   * A test-only API to allow controlling the list of users from tests.
   */
  suspend fun sendUsers(users: List<User>) {
    usersFlow.emit(users)
  }
}