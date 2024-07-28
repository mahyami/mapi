package com.google.mapi.data

import io.ktor.http.appendPathSegments


class MapsApiService(private val client: MapsHttpClient = MapsHttpClient()) {

    suspend fun getPlaceDetails(ftid: String): Result<String> {
        return client.getClient()
            .get {
                url {
                    appendPathSegments(DETAILS_PATH)
                    parameters.append(PARAM_FTID, ftid)
                }
            }
    }

    companion object {
        private const val DETAILS_PATH = "place/details/json"
        private const val PARAM_FTID = "ftid"
    }
}