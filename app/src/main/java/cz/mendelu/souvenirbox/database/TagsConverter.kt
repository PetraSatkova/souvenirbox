package cz.mendelu.souvenirbox.database

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class TagsConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromTags(tags: List<String>?): String? {
        return tags?.let { json.encodeToString(it) }   // e.g. ["beach","food"]
    }

    @TypeConverter
    fun toTags(value: String?): List<String>? {
        return value?.let { json.decodeFromString<List<String>>(it) }
    }
}