package ie.eoinhasty.edumate.data.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.data.studysessions.StudySession
import kotlinx.coroutines.tasks.await

class StudyGroupRepository {
    val db = Firebase.firestore
    val studyGroups = db.collection("study_groups")


    suspend fun addStudyGroup(studyGroup: StudyGroup) {
        studyGroups.add(studyGroup).await().id
    }

    suspend fun getStudyGroups() : List<StudyGroup> {
        return try {
            studyGroups.get().await().toObjects(StudyGroup::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSessionsForGroup(groupId: String) : List<StudySession> {
        return try {
            studyGroups.document(groupId)
                .collection("sessions")
                .get()
                .await()
                .toObjects(StudySession::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addSessionToGroup(groupId: String, session: StudySession) {
        studyGroups.document(groupId)
            .collection("sessions")
            .add(session)
            .await()
    }

    suspend fun deleteGroup(groupId: String) {
        studyGroups.document(groupId).delete().await()
    }

    suspend fun deleteSession(groupId: String, sessionId: String) {
        studyGroups.document(groupId)
            .collection("sessions")
            .document(sessionId)
            .delete()
            .await()
    }

    suspend fun updateSession(groupId: String, session: StudySession) {
        studyGroups.document(groupId)
            .collection("sessions")
            .document(session.sessionId)
            .set(session)
            .await()
    }

    suspend fun updateGroup(groupId: String, studyGroup: StudyGroup) {
        studyGroups.document(groupId)
            .set(studyGroup)
            .await()
    }

    suspend fun getGroup(groupId: String) : StudyGroup? {
        return try {
            studyGroups.document(groupId)
                .get()
                .await()
                .toObject(StudyGroup::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSession(groupId: String, sessionId: String) : StudySession? {
        return try {
            studyGroups.document(groupId)
                .collection("sessions")
                .document(sessionId)
                .get()
                .await()
                .toObject(StudySession::class.java)
        } catch (e: Exception) {
            null
        }
    }
}