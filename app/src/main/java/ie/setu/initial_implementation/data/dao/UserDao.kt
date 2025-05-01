package ie.setu.initial_implementation.data.dao

import androidx.room.*
import ie.setu.initial_implementation.data.models.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: String): User?
}