package com.example.rideon.model.database.firebase


import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class DriverManager private constructor() {

    object Config {
        const val COLLECTION_PASSENGERS = "passengers"
        const val COLLECTION_DRIVERS = "drivers"
        const val COLLECTION_RIDES = "rides"
        const val COLLECTION_RIDES_HISTORY = "rideHistory"
        const val COLLECTION_BOOKING_REQUESTS = "bookingRequests"
        const val ATTR_PASSENGER_ID = "passengerId"
        const val ATTR_DRIVER_ID = "driverId"
        const val ATTR_WALLET_BALANCE = "walletBalance"
        const val ATTR_STATUS = "status"
        const val ATTR_DATE = "date"
        const val ATTR_RIDE_ID = "rideId"
        const val ATTR_PRICE = "price"
        const val ATTR_AVAILABLE_SEATS = "availableSeats"

        const val VAL_STATUS_CANCELLED = "cancelled"
        const val VAL_STATUS_PENDING = "pending"
        const val VAL_STATUS_CONFIRMED = "confirmed"

        const val ERROR_UNEXPECTED = "Unexpected Error, please check your network"
        const val ERROR_NO_RIDES = "No Rides Found"
        const val ERROR_EXISTING_RIDE = "Ride already exists"
        const val ERROR_NO_AVAILABLE_SEATS = "No available seats"
        const val ERROR_NO_BOOKING_REQUESTS = "Booking request or ride not found"
        const val ERROR_TRANSACTION_FAILED = "Transaction failed"
    }

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
            fireStore.collection(Config.COLLECTION_DRIVERS)
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
        try {
            fireStore.collection(Config.COLLECTION_RIDES)
                .whereEqualTo(Config.ATTR_DRIVER_ID, driverId)
                .get()
                .addOnSuccessListener { result ->
                    val rides = result.documents.map { document ->
                        document.toObject(Ride::class.java)!!
                    }
                    onSuccess.invoke(rides)
                }.addOnFailureListener { onFailure.invoke(it) }
        } catch (e: Exception) { onFailure.invoke(e) }
    }

    fun getDriverPastRides(
        driverId: String,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_RIDES_HISTORY)
                .document(Config.COLLECTION_DRIVERS)
                .collection(driverId)
                .get()
                .addOnSuccessListener { result ->
                    val rides = result.documents.map {
                        it.toObject(Ride::class.java)!!
                    }
                    onSuccess.invoke(rides)
                }.addOnFailureListener { onFailure.invoke(it) }
        } catch (e: Exception){ onFailure.invoke(e) }
    }

    fun getBookingRequests(
        driverId: String,
        onSuccess: (List<BookingRequest>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .whereEqualTo(Config.ATTR_DRIVER_ID, driverId)
                .whereEqualTo(Config.ATTR_STATUS, Config.VAL_STATUS_PENDING)
                .get()
                .addOnSuccessListener { result ->
                    val bookings = result.documents.map { document ->
                        document.toObject(BookingRequest::class.java)!!
                    }
                    onSuccess.invoke(bookings)
                }
                .addOnFailureListener {
                    onFailure.invoke(it)
                }
        } catch (e: Exception) {onFailure.invoke(e)}
    }

    fun offerRide(
        ride: Ride,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            val ridesCollection = fireStore.collection(Config.COLLECTION_RIDES)
            val rideTimestamp = Timestamp(ride.date.time / 1000, 0)
            ridesCollection
                .whereEqualTo(Config.ATTR_DRIVER_ID, ride.driverId)
                .whereEqualTo(Config.ATTR_DATE, rideTimestamp)
                .count()
                .get(AggregateSource.SERVER)
                .addOnSuccessListener { it ->
                    if(it.count>0){
                        onFailure(Config.ERROR_EXISTING_RIDE)
                        return@addOnSuccessListener
                    }
                    ridesCollection
                        .add(ride)
                        .addOnSuccessListener {
                            val rideId = it.id
                            ridesCollection
                                .document(rideId)
                                .update(Config.ATTR_RIDE_ID, rideId)
                            onSuccess.invoke(rideId)
                        }
                        .addOnFailureListener {
                            onFailure.invoke(Config.ERROR_UNEXPECTED)
                        }
                }
        } catch (e: Exception) { onFailure.invoke(e.toString()) }
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
                fireStore.collection(Config.COLLECTION_RIDES)
                    .document(rideId)

            val bookingRequestsRef =
                fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                    .whereEqualTo(Config.ATTR_RIDE_ID, rideId)

            fireStore.runTransaction { transaction ->
                val rideSnapshot = transaction.get(rideRef)

                // Check if the ride exists and is offered by the specified driver
                if (rideSnapshot.exists() && rideSnapshot.getString(Config.ATTR_DRIVER_ID) == driverId) {
                    // Fetch data for each booking request
                    runBlocking {
                        val bookingRequestsSnapshot = bookingRequestsRef
                            .get()
                            .addOnSuccessListener { it.documents }.await()

                        // Remove the ride document and booking requests
                        transaction.delete(rideRef)

                        rideSnapshot.data?.let {
                            it[Config.ATTR_STATUS] = Config.VAL_STATUS_CANCELLED
                            addRideMapToHistory(
                                userId = driverId,
                                rideData = it,
                                userType = Config.COLLECTION_DRIVERS,
                                onSuccess = {},
                                onFailure = { onFailure(Config.ERROR_UNEXPECTED) }
                            )
                        }

                        bookingRequestsSnapshot.forEach { document ->
                            val passengerId = document.getString(Config.ATTR_PASSENGER_ID) ?: ""
                            transaction.delete(document.reference)

                            rideSnapshot.data?.let {
                                it[Config.ATTR_STATUS] = Config.VAL_STATUS_CANCELLED
                                addRideMapToHistory(
                                    userId = passengerId,
                                    userType = Config.COLLECTION_PASSENGERS,
                                    rideData = it,
                                    onSuccess = {},
                                    onFailure = { onFailure.invoke(Config.ERROR_UNEXPECTED) }
                                )
                            }
                        }
                    }
                } else {
                    onFailure.invoke(Config.ERROR_NO_RIDES)
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
        try {
            val historyRef = fireStore.collection(Config.COLLECTION_RIDES_HISTORY)
                .document(userType)
                .collection(userId)
                .document(rideData[Config.ATTR_RIDE_ID].toString())

            historyRef
                .set(rideData)
                .addOnSuccessListener { onSuccess.invoke() }
                .addOnFailureListener { onFailure.invoke(it) }
        }catch (e: Exception) { onFailure.invoke(e) }

    }

    fun addRideToHistory(
        userId: String,
        userType: String,
        ride: Ride,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_RIDES_HISTORY)
                .document(userType)
                .collection(userId)
                .add(ride)
                .addOnSuccessListener { onSuccess.invoke() }
                .addOnFailureListener { onFailure.invoke(it) }
        } catch (e: Exception) { onFailure.invoke(e) }
    }

    fun getRideData(
        rideId: String,
        onSuccess: (Ride) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_RIDES)
                .document(rideId)
                .get()
                .addOnSuccessListener { onSuccess.invoke(it.toObject(Ride::class.java)!!) }
                .addOnFailureListener { onFailure.invoke(it) }
        } catch (e: Exception) { onFailure.invoke(e) }
    }

    fun acceptBookings(
        requestId: String,
        rideId: String,
        onSuccess: (Double) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            var newBalance = 0.0
            val bookingRequestsRef = fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS).document(requestId)
            val rideRef = fireStore.collection(Config.COLLECTION_RIDES).document(rideId)

            fireStore.runTransaction { transaction ->
                val bookingSnapshot = transaction.get(bookingRequestsRef)
                val rideSnapshot = transaction.get(rideRef)

                if (bookingSnapshot.exists() && rideSnapshot.exists()) {
                    // Update booking request status to "accepted"
                    transaction.update(bookingRequestsRef,
                        Config.ATTR_STATUS,
                        Config.VAL_STATUS_CONFIRMED)


                    fireStore.runTransaction { t2 ->
                        val x = bookingSnapshot.get(Config.ATTR_PASSENGER_ID).toString()
                        val y = bookingSnapshot.get(Config.ATTR_DRIVER_ID).toString()
                        // Get the driver and the passenger
                        val passengerRef = fireStore.collection(Config.COLLECTION_PASSENGERS)
                            .document(x)
                        val driverRef = fireStore.collection(Config.COLLECTION_DRIVERS)
                            .document(y)

                        val passengerSnapshot = t2.get(passengerRef)
                        val driverSnapshot = t2.get(driverRef)

                        // Update the driver and passenger's wallet based on ride price
                        val passengerWallet = passengerSnapshot
                            .get(Config.ATTR_WALLET_BALANCE).toString().toDouble()

                        val driverWallet = driverSnapshot
                            .get(Config.ATTR_WALLET_BALANCE).toString().toDouble()

                        val ridePrice = rideSnapshot
                            .get(Config.ATTR_PRICE).toString().toDouble()

                        t2.update(passengerRef,
                            Config.ATTR_WALLET_BALANCE,
                            passengerWallet - ridePrice)

                        newBalance = driverWallet + ridePrice
                        t2.update(driverRef,
                            Config.ATTR_WALLET_BALANCE,
                            newBalance)
                    }


                    // Update available seats in the ride
                    val availableSeats = rideSnapshot.getLong(Config.ATTR_AVAILABLE_SEATS)
                    if (availableSeats != null && availableSeats > 0) {
                        transaction.update(rideRef,
                            Config.ATTR_AVAILABLE_SEATS,
                            availableSeats - 1)
                    } else {
                        // If no available seats, consider rolling back the transaction
                        onFailure.invoke(Exception(Config.ERROR_NO_AVAILABLE_SEATS))
                        return@runTransaction null
                    }

                    // Return a result to indicate a successful transaction
                    true
                } else {
                    // If either booking request or ride does not exist
                    onFailure.invoke(Exception(Config.ERROR_NO_BOOKING_REQUESTS))
                    null
                }
            }.addOnSuccessListener {
                onSuccess.invoke(newBalance)
            }.addOnFailureListener {
                onFailure.invoke(Exception(Config.ERROR_TRANSACTION_FAILED))
            }
        } catch (e: Exception) {onFailure.invoke(Exception(Config.ERROR_UNEXPECTED))}
    }

    fun finishRides(
        rideId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection("rides").document(rideId).get()
                .addOnSuccessListener { doc ->

                    val driverUid = doc.getString("driverId") ?: ""

                    val bookingRequestsRef = fireStore.collection("bookingRequests")
                    bookingRequestsRef.whereEqualTo("rideId", rideId).get()
                        .addOnSuccessListener {
                        val passengerRideHistoryRef =
                            fireStore.collection("rideHistory")
                                .document("passengers")
                        val driverRideHistoryRef =
                            fireStore.collection("rideHistory")
                                .document("drivers")

                        val batch = FirebaseFirestore.getInstance().batch()

                        // Remove ride
                        batch.delete(doc.reference)

                        // Remove related booking requests
                        for (bookingDoc in it.documents) {
                            batch.delete(bookingDoc.reference)

                            // Add to passenger ride history
                            val passengerUid = bookingDoc.getString("passengerId") ?: ""
                            val passengerHistoryRef =
                                passengerRideHistoryRef.collection(passengerUid).document(doc.id)
                            batch.set(passengerHistoryRef, doc.data!!)
                            batch.update(passengerHistoryRef, "status", "finished")
                        }

                        // Add to driver ride history
                        val driverHistoryRef =
                            driverRideHistoryRef.collection(driverUid).document(doc.id)
                        batch.set(driverHistoryRef, doc.data!!)
                        batch.update(driverHistoryRef, "status", "finished")

                        // Commit the batch for this ride
                        batch.commit().addOnSuccessListener { onSuccess.invoke() }
                            .addOnFailureListener {e -> onFailure.invoke(e) }
                    }
                }
        } catch (e: Exception) { onFailure.invoke(e) }
    }


    fun cancelBooking(
        requestId: String
    ) {
        try {
            // Cancel the booking by deleting the booking request
            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .document(requestId)
                .delete()
        } catch (e: Exception) {/* handle error */}
    }
}
