package com.example.lab345unichnu.dagger

import android.content.Context
import androidx.room.Room
import com.example.lab345unichnu.data.model.PhoneDAO
import com.example.lab345unichnu.data.model.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appLabDb"
        ).build()
    }
    @Provides
    fun providePhoneDao(database: AppDatabase): PhoneDAO{
        return database.phoneDao()
    }
}