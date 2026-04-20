package com.firkat.intervaltraining.core.di

import com.firkat.intervaltraining.core.resources.AndroidStringProvider
import com.firkat.intervaltraining.core.resources.StringProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourcesModule {

    @Binds
    @Singleton
    abstract fun bindStringProvider(impl: AndroidStringProvider): StringProvider
}
