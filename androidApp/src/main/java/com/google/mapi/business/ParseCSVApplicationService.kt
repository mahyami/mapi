package com.google.mapi.business

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ParseCSVApplicationService @Inject constructor() {

    fun getLocations(context: Context, defaultFileName: String): List<Location> {
        val locations = mutableListOf<Location>()
        val csv = readLatestCsv(context) ?: readCsvFromAssets(context, defaultFileName)
        val csvReader = BufferedReader(csv)
        csvReader.forEachLine { line ->
            val contents = line.split(",")
            if (contents.size != 4) {
                return@forEachLine
            }
            val urlString = contents[2]
            if (!urlString.startsWith("https://www.google.com/maps")) {
                return@forEachLine
            }
            val location = Location(
                name = contents[0],
                url = contents[2]
            )
            locations.add(location)
        }
        csvReader.close()
        csv.close()
        return locations
    }



    // TODO:: This will be updated when the user downloads the saved places
    private fun readCsvFromAssets(context: Context, fileName: String): InputStreamReader {
        val assetManager = context.assets
        return assetManager.open(fileName).reader()
    }

    private fun readLatestCsv(context: Context): InputStreamReader? {
        val file = context.filesDir.listFiles()?.find { it.name == "latest.csv" }
        if (file == null) {
            return null
        }
        return file.reader()
    }
}

data class Location(val name: String, val url: String)
