package com.example.rideon.view_models

import java.util.Date

class AvailableRides(
    pickup: String,
    dropOff: String,
    time: Date,
    price: Float,
    type: Int
) {
    private val pickup: String
    private val dropOff: String
    private val time: Date
    private val price: Float
    private val type: Int

    init {
        this.pickup = pickup
        this.dropOff = dropOff
        this.time = time
        this.price = price
        this.type = type
    }

    // Getters
    fun getPickup(): String {
        return pickup
    }

    fun getDropOff(): String {
        return dropOff
    }

    fun getTime(): Date {
        return time
    }

    fun getPrice(): Float {
        return price
    }

    fun getType(): Int {
        return type
    }

}