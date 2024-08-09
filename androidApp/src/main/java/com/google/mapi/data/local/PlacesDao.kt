package com.google.mapi.data.local

import androidx.room.*

@Dao
interface PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: LocalPlace)

    @Query("SELECT * FROM places WHERE placeId = :placeId")
    suspend fun getPlaceById(placeId: String): LocalPlace?

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces(): List<LocalPlace>

    @Query("SELECT COUNT(*) FROM places")
    suspend fun getPlacesCount(): Int

    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()
}
