package com.example.rideon.controller.home

import com.example.rideon.Config

object Config {
    object UserRole {
        val DRIVER = Config.UserRole.DRIVER
        val PASSENGER = Config.UserRole.PASSENGER
    }
    const val ROLE_PASSENGER = Config.ROLE_PASSENGER
    const val ROLE_DRIVER = Config.ROLE_DRIVER
    const val REQUIRED_FIELD = "This field is required"
    const val INTENT_UID = "uid"
    const val INTENT_EMAIL = "email"
    const val INTENT_NAME = "name"
    const val INTENT_WALLET_BALANCE = "wallet_balance"
    const val INVALID_USER = "Invalid user type"
    const val NO_INTERNET = "No internet connection"

}