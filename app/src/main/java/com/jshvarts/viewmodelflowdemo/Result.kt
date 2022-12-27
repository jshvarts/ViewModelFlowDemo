package com.jshvarts.viewmodelflowdemo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException

private const val RETRY_TIME_IN_MILLIS = 15_000L
private const val RETRY_ATTEMPT_COUNT = 3

sealed interface Result<out T> {
  data class Success<T>(val data: T) : Result<T>
  data class Error(val exception: Throwable? = null) : Result<Nothing>
  object Loading : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
  return this
    .map<T, Result<T>> {
      Result.Success(it)
    }
    .onStart { emit(Result.Loading) }
    .retryWhen { cause, attempt ->
      if (cause is IOException && attempt < RETRY_ATTEMPT_COUNT) {
        delay(RETRY_TIME_IN_MILLIS)
        true
      } else {
        false
      }
    }
    .catch { emit(Result.Error(it)) }
}