package com.tanya.core_database

import com.tanya.core_model.PeekDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseDaoModule {

    @Provides
    fun provideScanDao(db: PeekDatabase) = db.scanDao()

}