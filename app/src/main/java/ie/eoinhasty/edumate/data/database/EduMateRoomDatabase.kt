package ie.eoinhasty.edumate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.data.studygroups.StudyGroupDao
import ie.eoinhasty.edumate.data.studysessions.StudySession
import ie.eoinhasty.edumate.data.typeConverters.ListTypeConverter
import ie.eoinhasty.edumate.data.typeConverters.StudySessionListTypeConverter
import ie.eoinhasty.edumate.data.users.User
import ie.eoinhasty.edumate.data.users.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [StudyGroup::class, StudySession::class, User::class],
    version = 1
)
@TypeConverters(ListTypeConverter::class, StudySessionListTypeConverter::class)
abstract class EduMateRoomDatabase : RoomDatabase() {
    abstract fun studyGroupDao(): StudyGroupDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: EduMateRoomDatabase? = null
        private var coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: android.content.Context): EduMateRoomDatabase? {
            if (instance == null) {
                instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    EduMateRoomDatabase::class.java,
                    "edumate_database"
                ).build()
            }
            return instance
        }
    }
}
