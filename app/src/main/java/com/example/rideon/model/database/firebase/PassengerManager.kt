package com.example.rideon.model.database.firebase

import com.example.rideon.controller.passenger.Config
import com.example.rideon.model.data_classes.BookingRequest
import com.example.rideon.model.data_classes.Ride
import com.example.rideon.model.data_classes.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.atomic.AtomicInteger

class PassengerManager private constructor() {

    companion object {
        val instance: PassengerManager by lazy { PassengerManager() }
    }

    object Config {
        const val COLLECTION_PASSENGERS = "passengers"
        const val COLLECTION_DRIVERS = "drivers"
        const val COLLECTION_RIDES = "rides"
        const val COLLECTION_RIDES_HISTORY = "rideHistory"
        const val COLLECTION_BOOKING_REQUESTS = "bookingRequests"
        const val ATTR_USER_ID = "userId"
        const val ATTR_PASSENGER_ID = "passengerId"
        const val ATTR_DRIVER_ID = "driverId"
        const val ATTR_REQUEST_ID = "requestId"
        const val ATTR_EMAIL = "email"
        const val ATTR_NAME = "name"
        const val ATTR_USER_TYPE = "userType"
        const val ATTR_WALLET_BALANCE = "walletBalance"
        const val ATTR_STATUS = "status"
        const val ATTR_VEHICLE_TYPE = "vehicleType"


        const val VAL_STATUS_AVAILABLE = "Available"
        const val VAL_STATUS_CANCELLED = "cancelled"
        const val VAL_STATUS_PENDING = "pending"

        const val ERROR_UNEXPECTED = "Unexpected Error, please check your network"
        const val ERROR_NO_RIDES = "No Rides Found"
    }
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAvailableRides(
        passengerId: String,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .whereEqualTo(Config.ATTR_PASSENGER_ID, passengerId)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty){
                        return@addOnSuccessListener
                    } else {
                        fireStore.collection(Config.COLLECTION_RIDES)
                            .whereEqualTo(Config.ATTR_STATUS, Config.VAL_STATUS_AVAILABLE)
                            .get()
                            .addOnSuccessListener { result ->
                                val rides = result.documents.map { document ->
                                    document.toObject(Ride::class.java)!!
                                }
                                onSuccess.invoke(rides)
                            }
                            .addOnFailureListener { onFailure(Exception(Config.ERROR_UNEXPECTED)) }
                    }
                }.addOnFailureListener { onFailure(Exception(Config.ERROR_UNEXPECTED)) }
        }
        catch (_: Exception){ onFailure(Exception(Config.ERROR_UNEXPECTED)) }
    }

    fun getAvailableRidesByType(
        passengerId: String,
        vehicleType: Int,
        onSuccess: (List<Ride>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .whereEqualTo(Config.ATTR_PASSENGER_ID, passengerId)
                .get()
                .addOnSuccessListener {
                    if (!it.isEmpty){
                        return@addOnSuccessListener
                    } else {
                        fireStore.collection(Config.COLLECTION_RIDES)
                            .whereEqualTo(Config.ATTR_VEHICLE_TYPE, vehicleType)
                            .whereEqualTo(Config.ATTR_STATUS, Config.VAL_STATUS_AVAILABLE)
                            .get()
                            .addOnSuccessListener { result ->
                                val rides = result.documents.map { document ->
                                    document.toObject(Ride::class.java)!!
                                }
                                onSuccess.invoke(rides)
                            }.addOnFailureListener { onFailure(Exception(Config.ERROR_UNEXPECTED)) }
                    }
                }.addOnFailureListener { onFailure(Exception(Config.ERROR_UNEXPECTED)) }
        } catch(_: Exception) { onFailure(Exception(Config.ERROR_UNEXPECTED)) }
    }

    fun getPassengerBooking(
        passenger: User,
        onSuccess: (BookingRequest) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .whereEqualTo(Config.ATTR_PASSENGER_ID, passenger.userId)
                .get()
                .addOnSuccessListener { result ->
                    if(!result.isEmpty) {
                        val bookingRequest = result
                            .documents.first().toObject(BookingRequest::class.java)!!
                        onSuccess.invoke(bookingRequest)
                    } else {
                        onFailure.invoke(Exception(Config.ERROR_NO_RIDES))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure.invoke(exception)
                }
        }
        catch (e: Exception) {onFailure.invoke(e)}

    }

    fun getPassengerBookingDetails(
        rideId: String,
        driverId: String,
        onSuccess: (Ride, String) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_RIDES)
                .document(rideId)
                .get()
                .addOnSuccessListener {  ride ->
                    fireStore.collection(Config.COLLECTION_DRIVERS)
                        .document(driverId)
                        .get()
                        .addOnSuccessListener { driver ->
                            onSuccess.invoke(ride.toObject(Ride::class.java)!!,
                                driver.get(Config.ATTR_NAME).toString())
                        }
                }
        } catch (_:Exception){}
    }

    fun getPassengerPastRides(
        passenger: User,
        onSuccess: (List<Ride>, List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            fireStore.collection(Config.COLLECTION_RIDES_HISTORY)
                .document(Config.COLLECTION_PASSENGERS)
                .collection(passenger.userId)
                .get()
                .addOnSuccessListener { ridesResult ->
                    val driverIds = ArrayList<String>()
                    val rides = ridesResult.documents.map { document ->
                        document.toObject(Ride::class.java)!!
                    }

                    val remainingDriverCount = AtomicInteger(rides.size)

                    for (ride in rides) {
                        val driverRef = fireStore
                            .collection(Config.COLLECTION_DRIVERS)
                            .document(ride.driverId)
                        driverRef.get()
                            .addOnSuccessListener { driverSnapshot ->
                                val name = driverSnapshot.get(Config.ATTR_NAME).toString()
                                driverIds.add(name)

                                if (remainingDriverCount.decrementAndGet() == 0) {
                                    // All driver details fetched, invoke success callback
                                    onSuccess.invoke(rides, driverIds)
                                }
                            } .addOnFailureListener {e-> onFailure.invoke(e) } }
                }.addOnFailureListener { e -> onFailure.invoke(e) }
        }catch (e: Exception){onFailure.invoke(e)}
    }

    fun cancelBooking(
        bookingRequest: BookingRequest,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ){
        try {
            fireStore.collection(Config.COLLECTION_RIDES)
                .document(bookingRequest.rideId)
                .get()
                .addOnSuccessListener { doc ->
                    val ride = doc.toObject(Ride::class.java)!!
                    ride.status = Config.VAL_STATUS_CANCELLED
                    fireStore.collection(Config.COLLECTION_RIDES_HISTORY)
                        .document(Config.COLLECTION_PASSENGERS)
                        .collection(bookingRequest.passengerId)
                        .add(ride)
                        .addOnSuccessListener {
                            fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                                .document(bookingRequest.requestId)
                                .delete()
                                .addOnSuccessListener { onSuccess.invoke() }
                                .addOnFailureListener { onFailure.invoke(it) }
                        }.addOnFailureListener { onFailure.invoke(it) }
                }.addOnFailureListener { onFailure.invoke(it) }
        } catch (e: Exception) { onFailure.invoke(e) }
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

    fun updatePassengerWallet(
        userId: String,
        newBalance: Double
    ) {
        try {
            fireStore.collection(Config.COLLECTION_PASSENGERS)
                .document(userId)
                .update(Config.ATTR_WALLET_BALANCE, newBalance)
        } catch(_:Exception){}
    }

    fun bookRide(
        passenger: User,
        ride: Ride,
        onBooked: () -> Unit
    ) {
        try {
            val request = BookingRequest(
                passengerId = passenger.userId,
                driverId = ride.driverId,
                rideId = ride.rideId,
                status = Config.VAL_STATUS_PENDING,
                rideDate = ride.date,
                origin = ride.origin,
                destination = ride.destination,
                passengerEmail = passenger.email
            )

            val batch = fireStore.batch()

            // Add the booking request
            val bookingRequestRef = fireStore.collection(Config.COLLECTION_BOOKING_REQUESTS)
                .document()
            batch.set(bookingRequestRef, request)

            // Update the requestId field with the document ID
            batch.update(bookingRequestRef, Config.ATTR_REQUEST_ID, bookingRequestRef.id)

            // Set the ride document
            val rideRef = fireStore.collection(Config.COLLECTION_RIDES).document(ride.rideId)
            batch.set(rideRef, ride)

            // Commit the batch
            batch.commit()
                .addOnSuccessListener { onBooked.invoke() }
                .addOnFailureListener { /* Handle the error */ }
        } catch (e: Exception) { /* Handle the error */ }
    }
}
