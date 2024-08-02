package com.google.mapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        LocalPlace::class,
    ], version = 1
)
@TypeConverters(
    value = [
        Converters::class,
        ReviewConverters::class
    ]
)
abstract class MapiDatabase : RoomDatabase() {
    abstract fun placeDao(): PlacesDao
}
