package com.google.mapi.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ReviewConverters {
    @TypeConverter
    fun fromReviewList(value: List<LocalReview>): String {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun toReviewList(value: String): List<LocalReview> {
        val listType = object : TypeToken<List<LocalReview>>() {}.type
        return Gson().fromJson(value, listType)
    }
}