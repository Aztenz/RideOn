package com.example.rideon.model

import com.example.rideon.Config
import java.util.Date

object Config {

    const val DEFAULT_INT = 0
    const val DEFAULT_STRING = ""
    const val DEFAULT_DOUBLE = 0.0
    val DEFAULT_DATE = Date()
    const val ROLE_PASSENGER = Config.ROLE_PASSENGER
    const val ROLE_DRIVER = Config.ROLE_DRIVER

    val RIDE_TYPE_NAMES = arrayOf(
        "Bikes",
        "Cars",
        "Vans",
        "Busses",
    )

    const val COLLECTION_USERS = "users"
    const val COLLECTION_PASSENGERS = "passengers"
    const val COLLECTION_DRIVERS = "drivers"

    const val USER_ID_ATTRIBUTE = "userId"
    const val USER_TABLE = "user_table"
    const val USER_DATABASE = Config.USER_DATABASE

    const val QUERY_SELECT_ALL_USERS = "SELECT * FROM $USER_TABLE"
    const val QUERY_SELECT_USERS_WITH_USERID =
        "SELECT * FROM $USER_TABLE WHERE $USER_ID_ATTRIBUTE = :$USER_ID_ATTRIBUTE"
    const val QUERY_CLEAR_TABLE = "DELETE * FROM $USER_TABLE"

    const val ACCOUNT_BELONGS_TO_PASSENGER = "This account belongs to a passenger"
    const val ACCOUNT_BELONGS_TO_DRIVER = "This account belongs to a driver"
    const val USER_RETRIEVAL_ERROR = "Failed to retrieve user after registration"
    const val USER_UPDATE_ERROR = "User profile update failed"
    const val WRONG_CREDENTIALS = "Wrong Credentials"
    const val USER_NOT_FOUND = "No users found"
    const val UNEXPECTED_ERROR = "Unexpected error"
}