package com.example.rideon.model.database.firebase

import com.example.rideon.controller.passenger.Config
import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PassengerManager private constructor() {

    companion object {
        val instance: PassengerManager by lazy { PassengerManager() }
    }

    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAvailableRides(
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection("rides")
            .get()
            .addOnSuccessListener { result ->
                val rides = result.documents.map { document ->
                    document.toObject(Ride::class.java)!!
                }
                onSuccess(rides)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getAvailableRidesByType(
        vehicleType: Int,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        fireStore.collection("rides")
            .whereEqualTo("vehicleType", vehicleType)
            .get()
            .addOnSuccessListener { result ->
                val rides = result.documents.map { document ->
                    document.toObject(Ride::class.java)!!
                }
                onSuccess(rides)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getPassengerCurrentRide(
        passenger: User,
        onSuccess: (Ride?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        fireStore.collection("bookingRequests")
            .whereEqualTo("passengerId", passenger.userId)
            .whereEqualTo("status", "confirmed")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    onSuccess(null)
                } else {
                    val bookingRequest = result.documents.first().toObject(BookingRequest::class.java)!!
                    val rideId = bookingRequest.rideId

                    fireStore.collection("rides")
                        .document(rideId)
                        .get()
                        .addOnSuccessListener { document ->
                            val ride = document.toObject(Ride::class.java)
                            onSuccess(ride)
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getPassengerPastRides(
        passenger: User,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        fireStore.collection("passengerRideHistory")
            .document(passenger.userId)
            .collection("rideHistory")
            .get()
            .addOnSuccessListener { result ->
                val rides = result.documents.map { document ->
                    document.toObject(Ride::class.java)!!
                }
                onSuccess(rides)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getPassengerData(
        uid: String? = FirebaseAuth.getInstance().currentUser?.uid,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        try {
            if (uid != null) {
                fireStore.collection("passengers")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { document ->
                        try {
                            val user = document.toObject(User::class.java)!!
                            onSuccess(user)
                        } catch (e: Exception){
                            onFailure(e)
                        }
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            }
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    fun updatePassengerData(passenger: User) {

        fireStore.collection("passengers")
            .document(passenger.userId)
            .set(passenger)

    }

    fun updatePassengerWallet(userId: String, newBalance: Double) {
        fireStore.collection("passengers")
            .document(userId)
            .update("walletBalance", newBalance)
    }

    fun bookRide(
        passenger: User,
        ride: Ride
    ) {

        val request = BookingRequest(
            passengerId = passenger.userId,
            driverId = ride.driverId,
            rideId = ride.rideId,
            status = Config.RIDE_STATUS_PENDING,
            rideDate = ride.date,
            origin = ride.origin,
            destination = ride.destination,
            passengerEmail = passenger.email
        )

        fireStore.collection("bookingRequests")
            .add(request)
            .addOnSuccessListener {
                fireStore.collection("bookingRequests")
                    .document(it.id)
                    .update("requestId", it.id)
                    .addOnSuccessListener {
                        fireStore.collection("passengers")
                            .document(passenger.userId)
                            .update("walletBalance", passenger.walletBalance - ride.price)
                            .addOnSuccessListener {
                                ride.availableSeats--
                                if(ride.availableSeats==0)
                                    ride.status = "occupied"
                                fireStore.collection("rides")
                                    .document(ride.rideId)
                                    .set(ride)
                            }
                    }
            }
    }

    fun cancelBooking(requestId: String) {
        fireStore.collection("bookingRequests")
            .document(requestId)
            .delete()
    }

}
