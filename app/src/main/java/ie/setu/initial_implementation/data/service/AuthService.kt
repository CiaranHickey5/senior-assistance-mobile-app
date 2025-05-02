package ie.setu.initial_implementation.data.service

class AuthService {
    // Simple in-memory authentication for initial implementation
    private var isLoggedIn = false

    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Basic validation
        if (email.contains("@") && password.length >= 6) {
            isLoggedIn = true
            onSuccess()
        } else {
            onFailure("Invalid email or password")
        }
    }

    fun isUserSignedIn(): Boolean {
        return isLoggedIn
    }

    fun signOut() {
        isLoggedIn = false
    }
}