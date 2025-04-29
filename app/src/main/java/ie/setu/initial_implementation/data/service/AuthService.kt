package ie.setu.initial_implementation.data.service

import com.google.firebase.auth.FirebaseAuth

class AuthService {
    private val auth = FirebaseAuth.getInstance()

    /**
     * Login function takes email and password and attempts to sign in with Firebase Auth
     */
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // First try to sign in
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess()
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
                onSuccess()
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