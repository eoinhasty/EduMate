package ie.eoinhasty.edumate.ui.studygroups

/**
 * StudyGroupViewModel.kt
 * Manages study group and study session data, including operations for fetching, adding, and modifying groups and sessions.
 *
 * Features:
 * - Fetches study groups from Firestore, including those joined by the user.
 * - Allows adding, joining, and leaving study groups.
 * - Retrieves and manages study sessions for specific groups.
 * - Provides user and icon information for group members and UI enhancements.
 *
 * Author: [Eoin Hasty]
 * Date: [20/12/2024]
 */

import android.app.Application
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ie.eoinhasty.edumate.data.database.EduMateRoomDatabase
import ie.eoinhasty.edumate.data.database.UserRepository
import ie.eoinhasty.edumate.data.studygroups.StudyGroup
import ie.eoinhasty.edumate.data.studysessions.StudySession
import ie.eoinhasty.edumate.data.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel to handle all interactions related to Study Groups and Study Sessions.
 * Handles fetching, updating, and manipulating group and session data.
 *
 * @param application The application instance for database and repository access.
 */
class StudyGroupViewModel(application: Application): AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()

    private val _studyGroups = MutableStateFlow<List<StudyGroup>>(emptyList())
    val studyGroups: StateFlow<List<StudyGroup>> get() = _studyGroups

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> get() = _currentUser

    private val userRepository = UserRepository(
        userDao = EduMateRoomDatabase.getDatabase(application)!!.userDao(),
        firestore = FirebaseFirestore.getInstance()
    )

    /**
     * Fetches all study groups from Firestore.
     */
    fun fetchStudyGroups() {
        Log.d("StudyGroupViewModel", "Fetching study groups")

        _loading.value = true
        firestore.collection("studyGroups")
            .get()
            .addOnSuccessListener { result ->
                val groups = result.documents.mapNotNull { it.toObject(StudyGroup::class.java) }
                _studyGroups.value = groups
                _loading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message
                _loading.value = false
            }
    }

    /**
     * Retrieves a study group by its unique ID.
     *
     * @param groupId The unique identifier of the study group.
     * @return The StudyGroup object if found, or null otherwise.
     */
    fun getStudyGroupById(groupId: String): StudyGroup? {
        return _studyGroups.value.find { it.groupId == groupId }
    }

    /**
     * Fetches study groups joined by the current user.
     *
     * @param userId The user ID of the current user.
     */
    fun fetchJoinedStudyGroups(userId: String) {
        Log.d("StudyGroupViewModel", "Fetching joined study groups")

        _loading.value = true
        firestore.collection("studyGroups")
            .whereArrayContains("members", userId)
            .get()
            .addOnSuccessListener { result ->
                val groups = result.documents.mapNotNull { it.toObject(StudyGroup::class.java) }
                _studyGroups.value = groups
                _loading.value = false
            }
            .addOnFailureListener { exception ->
                _error.value = exception.message
                _loading.value = false
            }
    }

    /**
     * Adds a new study group to Firestore.
     *
     * @param studyGroup The StudyGroup object to be added.
     * @param onSuccess Callback for successful addition.
     * @param onFailure Callback for handling errors.
     */
    fun addStudyGroup(studyGroup: StudyGroup, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val groupData = mapOf(
            "groupId" to studyGroup.groupId,
            "groupName" to studyGroup.groupName,
            "description" to studyGroup.description,
            "meetingType" to studyGroup.meetingType,
            "year" to studyGroup.year,
            "schedule" to studyGroup.schedule,
            "category" to studyGroup.category,
            "iconName" to studyGroup.iconName,
            "members" to studyGroup.members.plus(studyGroup.createdBy),
            "memberCount" to studyGroup.memberCount + 1,
            "maxMembers" to studyGroup.maxMembers,
            "createdBy" to studyGroup.createdBy
        )

        firestore.collection("studyGroups")
            .document(studyGroup.groupId)
            .set(groupData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Unknown error") }
    }

    /**
     * Joins a study group by adding the user to the members list in Firestore.
     *
     * @param studyGroup The StudyGroup object representing the group to join.
     * @param userId The ID of the user joining the group.
     * @param onSuccess Callback for successful join operation.
     * @param onFailure Callback for handling errors, providing an error message.
     */
    fun joinStudyGroup(studyGroup: StudyGroup, userId: String?, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val groupData = mapOf(
            "members" to studyGroup.members.plus(userId),
            "memberCount" to studyGroup.memberCount + 1
        )

        firestore.collection("studyGroups")
            .document(studyGroup.groupId)
            .update(groupData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Unknown error") }
    }

    /**
     * Leaves a study group by removing the user from the members list in Firestore.
     *
     * @param studyGroup The StudyGroup object representing the group to leave.
     * @param userId The ID of the user leaving the group.
     * @param onSuccess Callback for successful leave operation.
     * @param onFailure Callback for handling errors, providing an error message.
     */
    fun leaveStudyGroup(studyGroup: StudyGroup, userId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val groupData = mapOf(
            "members" to studyGroup.members.minus(userId),
            "memberCount" to studyGroup.memberCount - 1
        )

        firestore.collection("studyGroups")
            .document(studyGroup.groupId)
            .update(groupData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Unknown error") }
    }

    /**
     * Retrieves the appropriate icon for the given icon name.
     *
     * @param iconName The name of the icon to retrieve.
     * @return The corresponding ImageVector for the given icon name, or a default icon if not found.
     */
    fun getIconForName(iconName: String): ImageVector {
        return iconMap[iconName] ?: Icons.Filled.QuestionMark
    }

    val iconMap: Map<String, ImageVector> = mapOf(
        "Search" to Icons.Filled.Search,
        "Home" to Icons.Filled.Home,
        "Settings" to Icons.Filled.Settings,
        "AccountCircle" to Icons.Filled.AccountCircle,
        "Star" to Icons.Filled.Star,
        "Group" to Icons.Filled.Group,
        "Notifications" to Icons.Filled.Notifications,
        "Email" to Icons.Filled.Email,
        "Favorite" to Icons.Filled.Favorite,
        "Info" to Icons.Filled.Info,
        "Place" to Icons.Filled.Place,
        "Phone" to Icons.Filled.Phone,
        "Camera" to Icons.Filled.Camera,
        "Lock" to Icons.Filled.Lock,
        "Person" to Icons.Filled.Person,
        "Calendar" to Icons.Filled.CalendarToday,
        "AccountTree" to Icons.Filled.AccountTree,
        "Security" to Icons.Filled.Security,
    )

    /**
     * Fetches the list of members in a study group from Firestore.
     *
     * @param groupId The unique ID of the study group.
     * @param onSuccess Callback providing a list of User objects representing the group members.
     * @param onFailure Callback for handling errors, providing an error message.
     */
    fun getGroupMembersFromFirestore(groupId: String, onSuccess: (List<User>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("studyGroups")
            .document(groupId)
            .get()
            .addOnSuccessListener { document ->
                val memberIds = document["members"] as? List<String> ?: emptyList()

                if(memberIds.isNotEmpty()) {
                    val users = mutableListOf<User>()
                    memberIds.forEach { memberId ->
                        getUserByID(memberId, { user ->
                            users.add(user)
                            if(users.size == memberIds.size) {
                                onSuccess(users)
                            }
                        }, { error ->
                            onFailure(error)
                        })
                    }
                } else {
                    onSuccess(emptyList())
                }
            }
    }

    /**
     * Fetches a user's details by their unique ID from Firestore.
     *
     * @param userId The unique ID of the user to fetch.
     * @param onSuccess Callback providing the User object if found.
     * @param onFailure Callback for handling errors, providing an error message.
     */
    fun getUserByID(userId: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        if (userId.isEmpty()) {
            onFailure("Invalid user ID")
            return
        }

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure("User not found")
                }
            }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Error fetching user") }
    }

    /**
     * Fetches the current user details from the local database and updates the state.
     */
    suspend fun getCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.uid?.let { userId ->
            userRepository.getUserByIdFromRoom(userId)?.let { user ->
                _currentUser.value = user
            }
        }
    }

    /**
     * Fetches all study sessions for a given study group from Firestore.
     *
     * @param groupId The unique ID of the study group.
     * @param onSuccess Callback providing a list of StudySession objects.
     * @param onFailure Callback for handling errors.
     */
    fun fetchStudySessions(groupId: String, onSuccess: (List<StudySession>) -> Unit, onFailure: () -> Unit) {
        firestore.collection("studySessions")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { result ->
                val sessions = result.documents.mapNotNull { it.toObject(StudySession::class.java) }
                onSuccess(sessions)
            }
            .addOnFailureListener { onFailure() }
    }

    /**
     * Adds a new study session to Firestore for a given study group.
     *
     * @param studySession The StudySession object representing the session to add.
     * @param onSuccess Callback for successful addition.
     * @param onFailure Callback for handling errors, providing an error message.
     */
    fun addStudySession(studySession: StudySession, onSuccess: () -> Boolean, onFailure: (String) -> Unit) {
        val sessionData = mapOf(
            "groupId" to studySession.groupId,
            "sessionTitle" to studySession.sessionTitle,
            "sessionDateTime" to studySession.sessionDateTime,
            "isOnline" to studySession.isOnline,
            "location" to studySession.location,
            "sessionDescription" to studySession.sessionDescription,
            "meetingLink" to studySession.meetingLink
        )

        firestore.collection("studySessions")
            .add(sessionData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { message ->
                onFailure(message.message ?: "Unknown error")
            }
    }
}
