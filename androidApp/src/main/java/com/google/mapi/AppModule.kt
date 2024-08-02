package com.google.mapi

import android.content.Context
import androidx.room.Room
import com.google.mapi.data.local.MapiDatabase
import com.google.mapi.data.local.PlacesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMapiDatabase(@ApplicationContext appContext: Context): MapiDatabase {
        return Room.databaseBuilder(
            appContext,
            MapiDatabase::class.java,
            "mapi_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlaceDao(mapiDatabase: MapiDatabase): PlacesDao = mapiDatabase.placeDao()

}