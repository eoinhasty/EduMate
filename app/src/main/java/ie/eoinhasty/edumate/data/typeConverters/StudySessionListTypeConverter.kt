package ie.eoinhasty.edumate.data.typeConverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ie.eoinhasty.edumate.data.studysessions.StudySession

class StudySessionListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String): List<StudySession> {
        val listType = object : TypeToken<List<StudySession>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<StudySession>): String {
        return gson.toJson(list)
    }
}
