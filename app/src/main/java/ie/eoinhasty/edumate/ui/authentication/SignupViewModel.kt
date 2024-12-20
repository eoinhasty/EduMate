package ie.eoinhasty.edumate.ui.authentication

/**
 * SignupViewModel.kt
 * This file contains the ViewModel implementation for user sign-up functionality.
 *
 * Features:
 * - Handles user registration and validation of input fields.
 * - Saves registered user details to Firestore.
 * - Manages state using a StateFlow for UI updates.
 *
 * Dependencies:
 * - FirebaseAuth for user authentication.
 * - FirebaseFirestore for saving user details.
 *
 * Author: [Your Name]
 * Date: [Insert Date]
 */

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ie.eoinhasty.edumate.data.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel to handle user sign-up logic.
 */
class SignupViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Mutable state for tracking sign-up progress and errors
    private val _signupState = MutableStateFlow<SignupState>(SignupState.Idle)
    val signupState: StateFlow<SignupState> get() = _signupState

    /**
     * Registers a new user with the provided details.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @param firstName The user's first name.
     * @param lastName The user's last name.
     * @param year The academic year of the user.
     * @param onSuccess Callback executed when sign-up is successful.
     * @param onFailure Callback executed when sign-up fails.
     */
    fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        year: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        // Validate input fields
        if (email.isEmpty() || password.isEmpty()) {
            _signupState.value = SignupState.Error("Email and password must not be empty")
            return
        }

        if (password.length < 6) {
            _signupState.value = SignupState.Error("Password must be at least 6 characters long")
            return
        }

        if (firstName.isEmpty() || lastName.isEmpty() || year.isEmpty()) {
            _signupState.value = SignupState.Error("First name, last name, and year must not be empty")
            return
        }

        // Launch sign-up process
        viewModelScope.launch {
            _signupState.value = SignupState.Loading
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the user ID from FirebaseAuth
                        val userId = task.result?.user?.uid ?: return@addOnCompleteListener

                        // Create a User object with the input data
                        val user = User(userId, firstName, lastName, email, year)

                        // Save user to Firestore
                        saveUserToFirestore(user, onSuccess, onFailure)

                        _signupState.value = SignupState.Success
                    } else {
                        _signupState.value = SignupState.Error(
                            task.exception?.message ?: "An error occurred"
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    _signupState.value = SignupState.Error(
                        exception.message ?: "An error occurred"
                    )
                }
        }
    }
}

/**
 * Represents the states of the sign-up process.
 */
sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    object Success : SignupState()
    data class Error(val message: String) : SignupState()
}

/**
 * Saves the user details to Firestore.
 *
 * @param user The user object containing details to be saved.
 * @param onSuccess Callback executed when saving is successful.
 * @param onFailure Callback executed when saving fails.
 */
fun saveUserToFirestore(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users")
        .document(user.userId)
        .set(user)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { exception ->
            onFailure(exception.message ?: "Error saving user")
        }
}