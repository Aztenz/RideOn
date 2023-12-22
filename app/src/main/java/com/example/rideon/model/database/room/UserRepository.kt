package com.example.rideon.model.database.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.rideon.model.data_classes.User

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserWallet(user: User, newBalance: Double) {
        user.walletBalance = newBalance
        userDao.updateUser(user)
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
