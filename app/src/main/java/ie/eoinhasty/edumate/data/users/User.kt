package ie.eoinhasty.edumate.data.users

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val userId: String = "",         // Unique Identifier for the user (Firebase UID)
    val firstName: String = "",      // First Name
    val lastName: String = "",       // Last Name
    val email: String = "",          // Email Address
    val year: String = "",           // Year (e.g., SD4, SD3)
)