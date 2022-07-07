package com.tanya.core_database

import android.content.Context
import android.os.Debug
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PeekRoomDatabase {
         val builder = Room
             .databaseBuilder(context, PeekRoomDatabase::class.java, "scans.db")
             .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        return builder.build()
    }

}