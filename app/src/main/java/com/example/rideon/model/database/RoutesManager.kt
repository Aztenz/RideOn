package com.example.rideon.model.database


import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class RoutesManager private constructor() {
    private val db = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: RoutesManager? = null

        fun getInstance(): RoutesManager {
            return instance ?: synchronized(this) {
                instance ?: RoutesManager().also { instance = it }
            }
        }
    }

    fun addAvailableRide(
        pickup: String,
        dropOff: String,
        time: Date,
        price: Double,
        type: Int,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val ridesCollection = db.collection("available_rides")

        val rideData = hashMapOf(
            "pickup" to pickup,
            "dropOff" to dropOff,
            "time" to time,
            "price" to price,
            "type" to type
        )

        ridesCollection.add(rideData)
            .addOnSuccessListener { documentReference ->
                onSuccess.invoke("Document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getAvailableRides(
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val ridesCollection = db.collection("available_rides")

        ridesCollection.get()
            .addOnSuccessListener { result ->
                val rides = mutableListOf<Ride>()

                for (document in result) {
                    val pickup = document.getString("pickup") ?: ""
                    val dropOff = document.getString("dropOff") ?: ""
                    val time = document.getDate("time") ?: Date()
                    val price = document.getDouble("price") ?: 0.0
                    val type = document.getLong("type")?.toInt() ?: 0

                    val ride = Ride(pickup, dropOff, time, price, type)
                    rides.add(ride)
                }

                onSuccess.invoke(rides)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getAvailableRidesOnType(
        rideType: Int,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val ridesCollection = db.collection("available_rides")

        ridesCollection.whereEqualTo("type", rideType)
            .get()
            .addOnSuccessListener { result ->
                val rides = mutableListOf<Ride>()
                for (document in result) {
                    val pickup = document.getString("pickup") ?: ""
                    val dropOff = document.getString("dropOff") ?: ""
                    val time = document.getDate("time") ?: Date()
                    val price = document.getDouble("price") ?: 0.0
                    val type = document.getLong("type")?.toInt() ?: 0
                    val ride = Ride(pickup, dropOff, time, price, type)
                    rides.add(ride)
                }

                onSuccess.invoke(rides)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    // Add more methods for other operations as needed

    // Data class for ride details
    data class Ride(
        val pickup: String,
        val dropOff: String,
        val time: Date,
        val price: Double,
        val type: Int
    )
}