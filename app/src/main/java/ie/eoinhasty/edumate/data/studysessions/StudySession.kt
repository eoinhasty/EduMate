package ie.eoinhasty.edumate.data.studysessions

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ie.eoinhasty.edumate.data.studygroups.StudyGroup

@Entity(
    tableName = "sessions",
    foreignKeys = [
        ForeignKey(
            entity = StudyGroup::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class StudySession(
    @PrimaryKey
    val sessionId: String = "",         // Unique identifier for the session
    val groupId: String = "",                       // Foreign key to reference the study group
    val sessionTitle: String = "",                  // Title of the session (e.g., "Intro to Algorithms")
    val sessionDescription: String = "",            // Description of the session
    val sessionDateTime: String = "",                 // Date and time of the session
    val location: String = "",                      // Location (e.g., "Online", or "Building B, Room 101")
    val isOnline: Boolean = false,                      // Whether the session is online
    val meetingLink: String? = null   // Optional meeting link for online sessions
)
