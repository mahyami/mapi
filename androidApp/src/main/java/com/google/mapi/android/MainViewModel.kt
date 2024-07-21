package com.google.mapi.android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.data.MapsApiService
import kotlinx.coroutines.launch


class MainViewModel(
    private val mapsApiService: MapsApiService = MapsApiService()
) : ViewModel() {

    fun getPlacesDetails() {
        viewModelScope.launch {
            // TODO::MM Next task
            mapsApiService.getPlaceDetails("0xd2464e31472fb4b:0x89b410b39db893a4")
                .map {
                    Log.d("PLACE:: ", it)
                }
        }
    }
}