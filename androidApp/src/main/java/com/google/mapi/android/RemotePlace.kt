package com.google.mapi.android

import com.google.gson.annotations.SerializedName

data class RemotePlace(
    @SerializedName("google_maps_url") val url: String,
    @SerializedName("location") val location: String,
)

data class RemoteLocation(
    @SerializedName("address") val address: String,
    @SerializedName("name") val name: String,
    @SerializedName("country_code") val countryCode: String,
)