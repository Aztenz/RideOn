package com.example.rideon.model.database.firebase

import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import java.util.*

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
        vehicleType: String,
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
        onSuccess: (Ride?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val passengerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        fireStore.collection("bookingRequests")
            .whereEqualTo("passengerId", passengerId)
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
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val passengerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        fireStore.collection("passengerRideHistory")
            .document(passengerId)
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

    fun updatePassengerData(user: User) {
        val passengerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        fireStore.collection("users")
            .document("passengers")
            .collection(passengerId)
            .document("userData")
            .set(user)
    }

    fun bookRide(
        passenger: User,
        ride: Ride
    ) {

        val request = BookingRequest(
            passengerId = passenger.userId,
            driverId = ride.driverId,
            rideId = ride.rideId,
            status = "pending",
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
                        val x = 5
                    }
            }
    }

    fun cancelBooking(requestId: String) {
        fireStore.collection("bookingRequests")
            .document(requestId)
            .delete()
    }

    fun updatePassengerWallet(balance: Int) {
        val passengerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        fireStore.collection("users")
            .document("passengers")
            .collection(passengerId)
            .document("userData")
            .update("walletBalance", FieldValue.increment(balance.toLong()))
    }

}
