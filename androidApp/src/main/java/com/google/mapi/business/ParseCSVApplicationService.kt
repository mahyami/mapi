package com.google.mapi.business

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class ParseCSVApplicationService @Inject constructor() {

    fun getLocations(context: Context): List<Location> {
        val locations = mutableListOf<Location>()
        val csv: InputStreamReader = readLatestCsv(context).takeIf { it != null } ?: run {
            Log.d("ParseCSVApplicationService:: ", "Reading from assets.")
            readDefaultCsvFromAssets(context)
        }
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


    private fun readDefaultCsvFromAssets(
        context: Context,
        fileName: String = "food.csv"
    ): InputStreamReader {
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
