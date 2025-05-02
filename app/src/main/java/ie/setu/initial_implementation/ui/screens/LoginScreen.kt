package ie.setu.initial_implementation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ie.setu.initial_implementation.DailyHavenApplication
import ie.setu.initial_implementation.data.repository.UserRepository
import ie.setu.initial_implementation.data.service.AuthService
import ie.setu.initial_implementation.ui.components.AccessibleTextField
import ie.setu.initial_implementation.ui.components.LargeAccessibleButton

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Get dependencies from the application
    val context = LocalContext.current
    val application = context.applicationContext as DailyHavenApplication
    val userDao = application.database.userDao()
    val userRepository = remember { UserRepository(userDao) }
    val authService = remember { AuthService() }

    // Check if user is already signed in
    LaunchedEffect(Unit) {
        if (authService.isUserSignedIn()) {
            onLoginClick()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Daily Haven",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Login or create an account",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email field
            AccessibleTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Enter your email",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            AccessibleTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter your password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login/Register button or loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                LargeAccessibleButton(
                    text = "Login / Register",
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank() && password.length >= 6) {
                            isLoading = true

                            authService.login(
                                email = email,
                                password = password,
                                onSuccess = {
                                    isLoading = false
                                    onLoginClick()
                                },
                                onFailure = { error ->
                                    isLoading = false
                                    errorMessage = error
                                    showError = true
                                }
                            )
                        } else if (password.length < 6 && password.isNotBlank()) {
                            errorMessage = "Password must be at least 6 characters"
                            showError = true
                        } else {
                            errorMessage = "Please enter both email and password"
                            showError = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Forgot password
            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

            // Error message
            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))
                Snackbar(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = errorMessage)
                }

                // Auto-hide error after 3 seconds
                LaunchedEffect(showError) {
                    if (showError) {
                        kotlinx.coroutines.delay(3000)
                        showError = false
                    }
                }
            }
        }
    }
}