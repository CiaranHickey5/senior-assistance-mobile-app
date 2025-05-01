package ie.setu.initial_implementation.data.service

import com.google.firebase.auth.FirebaseAuth
import ie.setu.initial_implementation.data.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Service class to handle authentication with Firebase
 */
class AuthService(private val userRepository: UserRepository) {
    private val auth = FirebaseAuth.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Login function takes email and password and attempts to sign in with Firebase Auth
     */
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // First try to sign in
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // User successfully logged in, ensure they exist in local database
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    coroutineScope.launch {
                        userRepository.createUser(userId, email)
                        onSuccess()
                    }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener {
                // If login fails, try to create the account
                register(email, password, onSuccess, onFailure)
            }
    }

    /**
     * Register a new user with Firebase Auth
     */
    private fun register(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // User successfully registered, save to local database
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    coroutineScope.launch {
                        userRepository.createUser(userId, email)
                        onSuccess()
                    }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Failed to authenticate")
            }
    }

    /**
     * Check if user is already signed in
     */
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Sign out the current user
     */
    fun signOut() {
        auth.signOut()
    }
}