package com.omarkarimli.movieapp.data.source.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGenreIdsList(genreIds: List<Int?>?): String? {
        return genreIds?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun toGenreIdsList(genreIdsString: String?): List<Int?>? {
        return genreIdsString?.let {
            val type = object : TypeToken<List<Int?>>() {}.type
            Gson().fromJson(it, type)
        }
    }
}
