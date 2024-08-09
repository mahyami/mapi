package com.google.mapi.domain

import com.google.mapi.converters.toLocalPlace
import com.google.mapi.data.local.PlacesDao
import com.google.mapi.data.remote.MapsApiService
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val mapsApiService: MapsApiService,
    private val placesDao: PlacesDao
) {
    suspend fun getPlaceDetails(ftid: String) {
        mapsApiService.getPlaceDetails(ftid)
            .onSuccess { remotePlace ->
                placesDao.insertPlace(remotePlace.toLocalPlace())
            }
    }

    suspend fun getPlacesCount() = placesDao.getPlacesCount()
}