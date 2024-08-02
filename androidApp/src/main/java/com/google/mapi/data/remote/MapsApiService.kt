package com.google.mapi.data.remote

import io.ktor.http.appendPathSegments
import javax.inject.Inject


class MapsApiService @Inject constructor(
    private val client: MapsHttpClient
) {

    suspend fun getPlaceDetails(ftid: String): Result<RemotePlace> {
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
