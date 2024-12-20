package ie.eoinhasty.edumate.data.studygroups

import androidx.room.Entity
import androidx.room.PrimaryKey
import ie.eoinhasty.edumate.data.studysessions.StudySession

@Entity(tableName = "study_groups"
)
data class StudyGroup(
    @PrimaryKey
    val groupId: String = "",          // Unique Identifier for the group (UUID)
    val groupName: String = "",        // Name of the group
    val description: String = "",      // Description of the study group (optional)
    val meetingType: String = "",      // Type of meeting (e.g., "Online", "In-Person")
    val year: String = "",             // The academic year of the group (e.g., "SD1")
    val schedule: String = "",         // The schedule of the group (e.g., "05/10/2021 - 05/10/2022")
    val category: String = "",         // The subject or category of the group (e.g., "Computer Science")
    val iconName: String = "",         // The name of the icon to be displayed for the group
    val members: List<String> = emptyList(), // List of member IDs
    val memberCount: Int = 0,          // Number of members in the group
    val maxMembers: Int = 0,            // Maximum number of members allowed in the group
    val createdBy: String = "",        // ID of the user who created the group
    val sessions: List<StudySession> = emptyList() // List of study sessions for the group
)