package com.jshvarts.viewmodelflowdemo.di

import com.jshvarts.viewmodelflowdemo.data.FakeUserRepository
import com.jshvarts.viewmodelflowdemo.data.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

  @Binds
  abstract fun bindsUserRepository(fakeUserRepository: FakeUserRepository): UserRepository
}