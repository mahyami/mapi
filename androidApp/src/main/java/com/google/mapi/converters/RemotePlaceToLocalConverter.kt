package com.google.mapi.converters

import com.google.mapi.data.local.LocalEditorialSummary
import com.google.mapi.data.local.LocalOpeningHours
import com.google.mapi.data.local.LocalPlace
import com.google.mapi.data.local.LocalPlaceResult
import com.google.mapi.data.local.LocalReview
import com.google.mapi.data.remote.EditorialSummary
import com.google.mapi.data.remote.OpeningHours
import com.google.mapi.data.remote.PlaceResult
import com.google.mapi.data.remote.RemotePlace
import com.google.mapi.data.remote.Review

fun RemotePlace.toLocalPlace(): LocalPlace {
    return LocalPlace(
        placeId = result.place_id,
        status = status,
        placeResult = result.toLocalPlaceResult()
    )
}

fun PlaceResult.toLocalPlaceResult(): LocalPlaceResult {
    return LocalPlaceResult(
        businessStatus = business_status,
        curbsidePickup = curbside_pickup,
        delivery = delivery,
        dineIn = dine_in,
        editorialSummary = editorial_summary.toLocalEditorialSummary(place_id),
        formattedAddress = formatted_address,
        formattedPhoneNumber = formatted_phone_number,
        icon = icon,
        iconBackgroundColor = icon_background_color,
        iconMaskBaseUri = icon_mask_base_uri,
        internationalPhoneNumber = international_phone_number,
        name = name,
        openingHours = opening_hours.toLocalOpeningHours(place_id),
        rating = rating,
        reference = reference,
        reservable = reservable,
        reviews = reviews.map { it.toLocalReview() },
        servesBeer = serves_beer,
        servesBreakfast = serves_breakfast,
        servesBrunch = serves_brunch,
        servesDinner = serves_dinner,
        servesLunch = serves_lunch,
        servesVegetarianFood = serves_vegetarian_food,
        servesWine = serves_wine,
        types = types,
        url = url,
        userRatingsTotal = user_ratings_total,
        utcOffset = utc_offset,
        vicinity = vicinity,
        website = website
    )
}

fun EditorialSummary.toLocalEditorialSummary(placeId: String): LocalEditorialSummary {
    return LocalEditorialSummary(
        language = language,
        overview = overview
    )
}

fun OpeningHours.toLocalOpeningHours(placeId: String): LocalOpeningHours {
    return LocalOpeningHours(
        openNow = open_now,
        weekdayText = weekday_text
    )
}

fun Review.toLocalReview(): LocalReview {
    return LocalReview(
        authorName = author_name,
        authorUrl = author_url,
        language = language,
        originalLanguage = original_language,
        profilePhotoUrl = profile_photo_url,
        rating = rating,
        relativeTimeDescription = relative_time_description,
        text = text,
        time = time,
        translated = translated
    )
}
