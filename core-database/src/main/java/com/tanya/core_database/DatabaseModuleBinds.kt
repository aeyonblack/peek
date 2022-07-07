package com.tanya.core_database

import com.tanya.core_model.PeekDatabase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModuleBinds {

    @Binds
    abstract fun bindPeekDatabase(db: PeekRoomDatabase): PeekDatabase

}