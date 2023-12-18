package com.example.rideon.model.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rideon.model.data_classes.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    fun getUser(userID: String): LiveData<User> {
        return userDao.getUser(userID)
    }

    fun getAllUsers(): LiveData<List<User>> {
        return userDao.getAllUsers()
    }

    fun getUsersCount(): LiveData<Int> {
        return userDao.getUsersCount()
    }

    suspend fun clearUsers() {
        userDao.clearUsers()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(context: Context): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val userDao = UserDao.getInstance(context)
                val instance = UserRepository(userDao)
                INSTANCE = instance
                instance
            }
        }
    }
}
