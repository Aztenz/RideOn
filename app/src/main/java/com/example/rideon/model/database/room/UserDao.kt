package com.example.rideon.model.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.rideon.model.data_classes.User

@Dao
interface UserDao {

    companion object {
        fun getInstance(context: Context): UserDao {
            val database = UserDatabase.getDatabase(context)
            return database.userDao()
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("DELETE FROM user_table")
    suspend fun clearUsers()

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    fun getUser(userId: String): LiveData<User>

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT COUNT(*) FROM user_table")
    fun getUsersCount(): LiveData<Int>

}
