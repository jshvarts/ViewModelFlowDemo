package com.jshvarts.viewmodelflowdemo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InMemoryUserRepository @Inject constructor() : UserRepository {
  override fun getUsers(): Flow<List<User>> = flow {
    val userList = listOf(
      User(
        name = "User 1",
        age = 20
      ),
      User(
        name = "User 2",
        age = 30
      ),
      User(
        name = "User 3",
        age = 60
      ),
      User(
        name = "User 4",
        age = 40
      ),
      User(
        name = "User 5",
        age = 50
      )
    )
    emit(userList.shuffled())
  }
}
