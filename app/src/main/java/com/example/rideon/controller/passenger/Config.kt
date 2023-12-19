package com.example.rideon.controller.passenger

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object Config {

    @SuppressLint("SimpleDateFormat")
    val DATE_PATTERN = SimpleDateFormat("d MMM - h:MM a")

    const val RIDE_STATUS_AVAILABLE = "Available"
    const val RIDE_STATUS_REJECTED = "rejected"
    const val ROLE_TYPE_PASSENGER = "passengers"
    const val RIDE_STATUS_PENDING = "pending"
    const val MORNING_HOUR = 7
    const val MORNING_MINUTE = 30
    const val REQUIRED_FIELDS = "please fill all the fields correctly"
    const val EVENING_HOUR = 17
    const val EVENING_MINUTE = MORNING_MINUTE
    const val SECONDS = 0
    const val MORNING_HOURS_TO_COMPARE = 9
    const val EVENING_HOURS_TO_COMPARE = 4
    const val TYPE_MOTORCYCLE = 0
    const val TYPE_CAR = 1
    const val TYPE_VAN = 2
    const val TYPE_BUS = 3
    const val MOTORCYCLE_SEATS = 2
    const val CAR_SEATS = 4
    const val VAN_SEATS = 10
    const val BUS_SEATS = 20
    const val DEFAULT_SEATS = 20
    const val TYPE_DEFAULT = TYPE_BUS
    const val GATE_3_REGEX = "\\b\\s*gate\\s*3\\s*\\b"
    const val GATE_4_REGEX = "\\b\\s*gate\\s*4\\s*\\b"
    const val GATE_3 = "Gate 3"
    const val GATE_4 = "Gate 4"
    const val LOG_TAG = "myapp101"
    const val INVALID_LOCATIONS = "Invalid Locations"
    const val INVALID_TIME_SELECTION = "Invalid time"
    const val MISSING_FIELDS = "Please fill all the fields"
    const val DRIVER_NOT_FOUND = "Can't find Driver"
    const val NO_INTERNET = "No internet connection"
    const val SUCCESS = "Success"
}