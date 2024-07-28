package com.google.mapi.domain

import android.util.Log
import com.google.mapi.data.MapsApiService

class PlacesRepository(
    private val mapsApiService: MapsApiService = MapsApiService(),
) {
    suspend fun getPlaceDetails(ftid: String) {
        mapsApiService.getPlaceDetails(ftid)
            .onSuccess {
                Log.d("PLACE:: ", it.result.toString())
            }
    }
}