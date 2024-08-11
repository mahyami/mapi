package com.google.mapi.domain

import com.google.mapi.converters.toLocalPlace
import com.google.mapi.data.local.PlacesDao
import com.google.mapi.data.remote.MapsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val mapsApiService: MapsApiService,
    private val placesDao: PlacesDao
) {

    fun getPlacesDetails(ids: List<String>): Flow<Boolean> = flow {
        val sizeOfPlaces = ids.size
        val detailsInserted: MutableList<PlaceResult.Success> = mutableListOf()
        ids
            .mapIndexed { index, id ->
                getPlaceDetails(id).collect {
                    if (index == sizeOfPlaces - 1) {
                        emit(detailsInserted.isNotEmpty())
                    }
                    if (it is PlaceResult.Success) {
                        detailsInserted.add(it)
                    }
                }
            }
    }

    private fun getPlaceDetails(id: String): Flow<PlaceResult> = flow {
        mapsApiService.getPlaceDetails(id)
            .map {
                placesDao.insertPlace(it.toLocalPlace())
                emit(PlaceResult.Success(it.result.place_id))
            }
            .onFailure {
                emit(PlaceResult.Failure)
            }
    }

    sealed interface PlaceResult {
        data class Success(val placeId: String) : PlaceResult
        data object Failure : PlaceResult
    }

    suspend fun getPlacesCount() = placesDao.getPlacesCount()
}
