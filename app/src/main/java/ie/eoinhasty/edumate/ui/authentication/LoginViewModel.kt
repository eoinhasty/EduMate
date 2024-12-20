/**
 * LoginViewModel.kt
 * This file contains the LoginViewModel implementation, responsible for managing the login process and state.
 *
 * Features:
 * - Handles login functionality using Firebase Authentication and Firestore.
 * - Updates UI state based on login success or failure.
 * - Includes a helper function to fetch user details from Firestore.
 *
 * Dependencies:
 * - Firebase Authentication and Firestore for user authentication and data retrieval.
 * - EduMateRoomDatabase for local user data storage.
 *
 * Author: [Eoin Hasty]
 * Date: [16/12/2024]
 */

package ie.eoinhasty.edumate.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import ie.eoinhasty.edumate.data.database.EduMateRoomDatabase
import ie.eoinhasty.edumate.data.database.UserRepository
import ie.eoinhasty.edumate.data.users.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to handle user login and state management.
 *
 * @param application The application instance for database and repository access.
 */
class LoginViewModel (application: Application): AndroidViewModel(application) {
    private val repository = UserRepository(
        userDao = EduMateRoomDatabase.getDatabase(application)!!.userDao(),
        firestore = FirebaseFirestore.getInstance()
    )

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    /**
     * Handles user login using email and password.
     *
     * @param email User's email address.
     * @param password User's password.
     * @param onSuccess Callback triggered upon successful login.
     * @param onFailure Callback triggered upon login failure with an error message.
     */
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            repository.loginUser(
                email = email,
                password = password,
                onSuccess = {
                    viewModelScope.launch(Dispatchers.Main){
                        _loginState.value = LoginState.Success
                        onSuccess()
                    }
                },
                onFailure = { error ->
                    _loginState.value = LoginState.Error(error)
                    onFailure(error)
                }
            )
        }
    }

    /**
     * Resets the login state to Idle.
     */
    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

/**
 * Represents the various states of the login process.
 */
sealed class LoginState {
    object Idle: LoginState()
    object Loading: LoginState()
    object Success: LoginState()
    data class Error(val message: String): LoginState()
}

/**
 * Fetches user details from Firestore based on the user ID.
 *
 * @param userId The ID of the user to fetch.
 * @param onSuccess Callback triggered upon successful user retrieval with the user object.
 * @param onFailure Callback triggered upon user retrieval failure with an error message.
 */
fun fetchUserFromFirestore(userId: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
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