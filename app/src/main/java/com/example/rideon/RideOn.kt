package com.example.rideon

import android.app.Application
import androidx.room.Room
import com.example.rideon.model.Config.USER_DATABASE
import com.example.rideon.model.database.room.UserDatabase

class RideOn: Application() {

    companion object {
        lateinit var instance: RideOn
        lateinit var database: UserDatabase
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = Room.databaseBuilder(
            applicationContext,
            UserDatabase::class.java,
            USER_DATABASE
        ).build()
    }
}