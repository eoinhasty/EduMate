package ie.eoinhasty.edumate.data.database

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ie.eoinhasty.edumate.data.users.User
import ie.eoinhasty.edumate.data.users.UserDao
import ie.eoinhasty.edumate.ui.authentication.fetchUserFromFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository(
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore
) {
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (email.isEmpty() || password.isEmpty()) {
            onFailure("Email and password must not be empty")
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val authUser = result.user
                authUser?.let { user ->
                    firestore.collection("users")
                        .document(user.uid)
                        .get()
                        .addOnSuccessListener { document ->
                            val user = document.toObject(User::class.java)
                            user?.let { savedUser ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    saveUserToRoom(savedUser, onSuccess)
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception.message ?: "An error occurred")
                        }
                }

            }
    }

    private suspend fun saveUserToRoom(user: User, onSuccess: (String) -> Unit) {
        userDao.deleteAllUsers()
        userDao.insertUser(user)
        onSuccess(user.userId)
    }

    suspend fun getUserByIdFromRoom(id: String): User? {
        return userDao.getUserById(id)
    }
}