package ie.eoinhasty.edumate.data.studygroups

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ie.eoinhasty.edumate.data.studysessions.StudySession
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyGroupDao {
    @Insert
    suspend fun insertGroup(studyGroup: StudyGroup)

    @Insert
    suspend fun insertSession(session: StudySession)

    @Query("SELECT * FROM study_groups")
    fun getAllGroups(): Flow<List<StudyGroup>>

    @Query("DELETE FROM study_groups WHERE groupId = :groupId")
    suspend fun deleteGroup(groupId: String)
}
