package com.google.mapi.business

import android.content.Context
import javax.inject.Inject

class GetPlacesIdsApplicationService @Inject constructor(
    private val parseCSVApplicationService: ParseCSVApplicationService,
) {

    operator fun invoke(context: Context): List<String> {
        return parseCSVApplicationService.getLocations(
            context = context,
        ).mapNotNull { location ->
            extractFtIdFromUrl(location.url)
        }
    }

    private fun extractFtIdFromUrl(url: String): String? {
        val sequence = "!4m2!3m1!1s"
        val index = url.indexOf(sequence)

        return if (index != -1) {
            val startIndex = index + sequence.length
            val endIndex = url.indexOf("/", startIndex).takeIf { it != -1 } ?: url.length
            url.substring(startIndex, endIndex)
        } else {
            null
        }
    }

}