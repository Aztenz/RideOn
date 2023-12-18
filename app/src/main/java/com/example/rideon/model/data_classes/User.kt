package com.example.rideon.model.data_classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.rideon.model.Config

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey
    val userId: String = Config.DEFAULT_STRING,
    val email: String = Config.DEFAULT_STRING,
    val name: String = Config.DEFAULT_STRING,
    val walletBalance: Double = Config.DEFAULT_DOUBLE,
    var userType: String = Config.DEFAULT_STRING
    // Add other user details as needed
)
