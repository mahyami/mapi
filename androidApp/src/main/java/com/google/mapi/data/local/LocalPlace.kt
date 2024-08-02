package com.google.mapi.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "places")
data class LocalPlace(
    @PrimaryKey val placeId: String,
    val status: String,
    @Embedded val placeResult: LocalPlaceResult
)

data class LocalPlaceResult(
    val businessStatus: String,
    val curbsidePickup: Boolean,
    val delivery: Boolean,
    val dineIn: Boolean,
    @Embedded val editorialSummary: LocalEditorialSummary,
    val formattedAddress: String,
    val formattedPhoneNumber: String,
    val icon: String,
    val iconBackgroundColor: String,
    val iconMaskBaseUri: String,
    val internationalPhoneNumber: String,
    val name: String,
    @Embedded val openingHours: LocalOpeningHours,
    val rating: Double,
    val reference: String,
    val reservable: Boolean,
    @TypeConverters(ReviewConverters::class) val reviews: List<LocalReview>,
    val servesBeer: Boolean,
    val servesBreakfast: Boolean,
    val servesBrunch: Boolean,
    val servesDinner: Boolean,
    val servesLunch: Boolean,
    val servesVegetarianFood: Boolean,
    val servesWine: Boolean,
    val types: List<String>,
    val url: String,
    val userRatingsTotal: Int,
    val utcOffset: Int,
    val vicinity: String,
    val website: String
)

data class LocalEditorialSummary(
    val language: String,
    val overview: String
)

data class LocalOpeningHours(
    val openNow: Boolean,
    val weekdayText: List<String>
)

data class LocalReview(
    val authorName: String,
    val authorUrl: String,
    val language: String,
    val originalLanguage: String,
    val profilePhotoUrl: String,
    val rating: Int,
    val relativeTimeDescription: String,
    val text: String,
    val time: Long,
    val translated: Boolean
)
