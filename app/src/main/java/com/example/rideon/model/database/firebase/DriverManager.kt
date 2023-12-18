package com.example.rideon.model.database.firebase


import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class DriverManager private constructor() {

    companion object {
        val instance: DriverManager by lazy { DriverManager() }
    }

    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getDriverData(
        driverId: String,
        onSuccess: (User) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        try {
            fireStore.collection("drivers")
                .document(driverId)
                .get()
                .addOnSuccessListener { document ->
                    try {
                        val user = document.toObject(User::class.java)!!
                        onSuccess(user)
                    } catch (e: Exception){ onFailure(e) }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    fun getDriverCurrentRides(
        driverId: String,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        fireStore.collection("rides")
            .whereEqualTo("driverId", driverId)
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

    fun getDriverPastRides(
        driverId: String,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection("rideHistory")
            .document("drivers")
            .collection(driverId)
            .get()
            .addOnSuccessListener { result ->
                val rides = result.documents.map {
                    it.toObject(Ride::class.java)!!
                }
                onSuccess(rides)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getBookingRequests(
        driverId: String,
        onSuccess: (List<BookingRequest>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        fireStore.collection("bookingRequests")
            .whereEqualTo("driverId", driverId)
            .whereEqualTo("status", "pending")
            .get()
            .addOnSuccessListener { result ->
                val bookings = result.documents.map { document ->
                    document.toObject(BookingRequest::class.java)!!
                }
                onSuccess(bookings)
            }
            .addOnFailureListener {
                onFailure(it)
            }
    }

    fun offerRide(
        ride: Ride,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {

        try {
            val ridesCollection = fireStore.collection("rides")
            val rideTimestamp = Timestamp(ride.date.time / 1000, 0)
            ridesCollection
                .whereEqualTo("driverId", ride.driverId)
                .whereEqualTo("date", rideTimestamp)
                .count()
                .get(AggregateSource.SERVER)
                .addOnSuccessListener { it ->
                    if(it.count>0){
                        onFailure("Ride already exists")
                        return@addOnSuccessListener
                    }
                    ridesCollection
                        .add(ride)
                        .addOnSuccessListener {
                            val rideId = it.id
                            ridesCollection
                                .document(rideId)
                                .update("rideId", rideId)
                            onSuccess(rideId)
                        }
                        .addOnFailureListener {
                            onFailure("Error adding this ride")
                        }
                }
        } catch (e: Exception) { onFailure(e.toString()) }
    }

    fun deleteBookingRequest(requestId: String) {
        fireStore.collection("bookingRequests")
            .document(requestId)
            .delete()
    }

    fun cancelOfferedRide(
        rideId: String,
        driverId: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        try {
            // Step 1: Find the ride document
            val rideRef =
                fireStore.collection("rides")
                    .document(rideId)

            val bookingRequestsRef =
                fireStore.collection("bookingRequests")
                    .whereEqualTo("rideId", rideId)

            fireStore.runTransaction { transaction ->
                val rideSnapshot = transaction.get(rideRef)

                // Check if the ride exists and is offered by the specified driver
                if (rideSnapshot.exists() && rideSnapshot.getString("driverId") == driverId) {
                    // Fetch data for each booking request
                    runBlocking {
                        val bookingRequestsSnapshot = bookingRequestsRef
                            .get()
                            .addOnSuccessListener { it.documents }.await()

                        // Remove the ride document and booking requests
                        transaction.delete(rideRef)

                        rideSnapshot.data?.let {
                            it["status"] = "cancelled"
                            addRideMapToHistory(
                                userId = driverId,
                                rideData = it,
                                userType = "drivers",
                                onSuccess = {},
                                onFailure = { onFailure("Can't add drive to history") }
                            )
                        }
                        bookingRequestsSnapshot.forEach { document ->
                            val passengerId = document.getString("passengerId") ?: ""
                            transaction.delete(document.reference)
                            // Add the ride to ride history for both driver and passengers with status as "cancelled"
                            rideSnapshot.data?.let {
                                addRideMapToHistory(
                                    userId = passengerId,
                                    userType = "passengers",
                                    rideData = it,
                                    onSuccess = {},
                                    onFailure = { onFailure("Can't add drive to history") }
                                )
                            }
                        }
                    }
                } else {
                    onFailure("Ride not found or not offered by the specified driver.")
                }

                null // Return a dummy result to complete the transaction
            }.addOnSuccessListener { onSuccess() }
        } catch (e: Exception) { onFailure(e.toString()) }
    }

    private fun addRideMapToHistory(
        userId: String,
        userType: String,
        rideData: MutableMap<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val historyRef = fireStore.collection("rideHistory")
            .document(userType)
            .collection(userId)
            .document(rideData["rideId"].toString())

        historyRef
            .set(rideData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }

    }

    fun addRideToHistory(
        userId: String,
        userType: String,
        ride: Ride,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection("rideHistory")
            .document(userType)
            .collection(userId)
            .add(ride)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke(it) }
    }

    fun getRideData(
        rideId: String,
        onSuccess: (Ride) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection("rides")
            .document(rideId)
            .get()
            .addOnSuccessListener { onSuccess.invoke(it.toObject(Ride::class.java)!!) }
            .addOnFailureListener { onFailure.invoke(it) }
    }

    fun acceptBookings(
        requestId: String,
        rideId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val bookingRequestsRef = fireStore.collection("bookingRequests").document(requestId)
        val rideRef = fireStore.collection("rides").document(rideId)

        fireStore.runTransaction { transaction ->
            val bookingSnapshot = transaction.get(bookingRequestsRef)
            val rideSnapshot = transaction.get(rideRef)

            if (bookingSnapshot.exists() && rideSnapshot.exists()) {
                // Update booking request status to "accepted"
                transaction.update(bookingRequestsRef, "status", "accepted")

                // Update available seats in the ride
                val availableSeats = rideSnapshot.getLong("availableSeats")
                if (availableSeats != null && availableSeats > 0) {
                    transaction.update(rideRef, "availableSeats", availableSeats - 1)
                } else {
                    // If no available seats, consider rolling back the transaction
                    onFailure.invoke(Exception("No available seats"))
                    return@runTransaction null
                }

                // Return a result to indicate a successful transaction
                true
            } else {
                // If either booking request or ride does not exist
                onFailure.invoke(Exception("Booking request or ride not found"))
                null
            }
        }.addOnSuccessListener {
            onSuccess.invoke()
        }.addOnFailureListener {
            onFailure.invoke(Exception("Transaction failed"))
        }
    }





    fun updateDriverWallet(
        balance: Int,
        driverId: String,
        ) {

        fireStore.collection("drivers")
            .document(driverId)
            .update("walletBalance", FieldValue.increment(balance.toLong()))
    }

    fun updateDriverData(
        driverId: String,
        user: User
    ) {

        fireStore.collection("users")
            .document("drivers")
            .collection(driverId)
            .document("userData")
            .set(user)
    }

    fun cancelBooking(
        requestId: String
    ) {
        // Cancel the booking by deleting the booking request
        fireStore.collection("bookingRequests")
            .document(requestId)
            .delete()
    }
}
