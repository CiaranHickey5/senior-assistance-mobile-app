package ie.setu.initial_implementation.data.repository

import ie.setu.initial_implementation.data.dao.UserDao
import ie.setu.initial_implementation.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


// Bridge between the ViewModel and the data sources (Room Database).

class UserRepository(private val userDao: UserDao) {

    suspend fun createUser(userId: String, email: String) {
        withContext(Dispatchers.IO) {
            // Create a user
            val user = User(
                userId = userId,
                email = email
            )
            userDao.insert(user)
        }
    }

    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }
}