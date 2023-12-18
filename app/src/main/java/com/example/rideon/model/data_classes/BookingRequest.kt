package com.example.rideon.model.data_classes

import com.example.rideon.model.Config
import java.util.Date

data class BookingRequest(
    val requestId: String = Config.DEFAULT_STRING,
    val passengerId: String = Config.DEFAULT_STRING,
    val driverId: String = Config.DEFAULT_STRING,
    val rideId: String = Config.DEFAULT_STRING,
    var status: String = Config.DEFAULT_STRING,
    var rideDate: Date = Config.DEFAULT_DATE,
    val origin: String = Config.DEFAULT_STRING,
    val destination: String = Config.DEFAULT_STRING,
    val passengerEmail: String = Config.DEFAULT_STRING
)
