package com.google.mapi.data.remote

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RemotePlace(
    @SerializedName("result") val result: PlaceResult,
    val status: String = ""
)

@Serializable
data class PlaceResult(
    val business_status: String = "",
    val curbside_pickup: Boolean = false,
    val delivery: Boolean = false,
    val dine_in: Boolean = false,
    val editorial_summary: EditorialSummary = EditorialSummary(),
    val formatted_address: String = "",
    val formatted_phone_number: String = "",
    val icon: String = "",
    val icon_background_color: String = "",
    val icon_mask_base_uri: String = "",
    val international_phone_number: String = "",
    val name: String = "",
    val opening_hours: OpeningHours = OpeningHours(),
    val place_id: String = "",
    val rating: Double = 0.0,
    val reference: String = "",
    val reservable: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val serves_beer: Boolean = false,
    val serves_breakfast: Boolean = false,
    val serves_brunch: Boolean = false,
    val serves_dinner: Boolean = false,
    val serves_lunch: Boolean = false,
    val serves_vegetarian_food: Boolean = false,
    val serves_wine: Boolean = false,
    val types: List<String> = emptyList(),
    val url: String = "",
    val user_ratings_total: Int = 0,
    val utc_offset: Int = 0,
    val vicinity: String = "",
    val website: String = ""
)

@Serializable
data class EditorialSummary(
    val language: String = "",
    val overview: String = ""
)

@Serializable
data class OpeningHours(
    val open_now: Boolean = false,
    val weekday_text: List<String> = emptyList()
)

@Serializable
data class Review(
    val author_name: String = "",
    val author_url: String = "",
    val language: String = "",
    val original_language: String = "",
    val profile_photo_url: String = "",
    val rating: Int = 0,
    val relative_time_description: String = "",
    val text: String = "",
    val time: Long = 0L,
    val translated: Boolean = false
)
