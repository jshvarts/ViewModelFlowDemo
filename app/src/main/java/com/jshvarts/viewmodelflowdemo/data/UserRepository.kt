package com.jshvarts.viewmodelflowdemo.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
  fun getUsers(): Flow<List<User>>
}