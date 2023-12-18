package com.example.rideon.model.data_classes

import com.example.rideon.model.Config
import java.util.Date

data class Ride(
    val rideId: String = Config.DEFAULT_STRING,
    val driverId: String = Config.DEFAULT_STRING,
    val origin: String = Config.DEFAULT_STRING,
    val destination: String = Config.DEFAULT_STRING,
    val availableSeats: Int = Config.DEFAULT_INT,
    val vehicleType: Int = Config.DEFAULT_INT,
    val date: Date = Config.DEFAULT_DATE,
    val price: Double = Config.DEFAULT_DOUBLE,
    var status: String = Config.DEFAULT_STRING,
    // Add other ride details as needed
)
