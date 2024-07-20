package com.google.mapi.business

import java.io.File

class ParseCSV(private val file: File) {

    // Parses the CSV file and returns a list of Location objects
    public fun getLocations(): List<Location> {
        val locations = mutableListOf<Location>()
        file.forEachLine { line ->
            val contents = line.split(",")
            if (contents.size != 4) {
                return@forEachLine
            }
            val urlString = contents[3]
//            ensure that the URL is a Google Maps URL
            if (!urlString.startsWith("https://www.google.com/maps")) {
                return@forEachLine
            }
            val location = Location(
                name = contents[0],
                url = contents[3]
            )
            locations.add(location)
        }
        return locations
    }
}

data class Location(val name: String, val url: String)
