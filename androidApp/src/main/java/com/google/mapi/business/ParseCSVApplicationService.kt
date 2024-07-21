package com.google.mapi.business

import android.content.Context
import java.io.BufferedReader

class ParseCSVApplicationService {

    fun getLocations(context: Context, fileName: String): List<Location> {
        val locations = mutableListOf<Location>()
        val csvLines = readCsvFromAssets(context, fileName)
        csvLines.forEach { line ->
            val contents = line.split(",")
            if (contents.size != 4) {
                return@forEach
            }
            val urlString = contents[2]
            if (!urlString.startsWith("https://www.google.com/maps")) {
                return@forEach
            }
            val location = Location(
                name = contents[0],
                url = contents[2]
            )
            locations.add(location)
        }
        return locations
    }

    // TODO:: This will be updated when the user downloads the saved places
    private fun readCsvFromAssets(context: Context, fileName: String): List<String> {
        val assetManager = context.assets
        val inputStreamReader = assetManager.open(fileName).reader()
        val reader = BufferedReader(inputStreamReader)

        val result = mutableListOf<String>()
        reader.forEachLine { line ->
            result.add(line)
        }

        return result
    }
}

data class Location(val name: String, val url: String)
