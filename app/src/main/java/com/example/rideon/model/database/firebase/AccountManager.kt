package com.example.rideon.model.database.firebase

import android.util.Log
import com.example.rideon.model.data_classes.User
import com.example.rideon.model.Config
import com.example.rideon.Config.UserRole
import com.example.rideon.model.database.room.RoomAccountManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AccountManager private constructor()  {

    companion object {
        val instance: AccountManager by lazy { AccountManager() }
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val roomInstance: RoomAccountManager = RoomAccountManager.instance


    fun registerUser(
        userType: String,
        displayName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        createUser(displayName, email, password,
            {
                val user = auth.currentUser
                user?.let {
                    val userId = it.uid
                    val userData = User(
                        userId = userId,
                        email = email,
                        name = displayName,
                        walletBalance = Config.DEFAULT_DOUBLE)

                    addUserDataToFireStore(
                        userType = userType,
                        userId = userId,
                        userData = userData,
                        onSuccess = onSuccess,
                        onFailure = onFailure
                    )
                } ?: onFailure(Exception(Config.USER_RETRIEVAL_ERROR))
            },
            onFailure
        )
    }

    private fun createUser(
        displayName: String,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.updateProfile(
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                )?.addOnCompleteListener { task ->
                    if (task.isSuccessful) onSuccess.invoke()
                    else onFailure.invoke(task.exception ?: Exception(Config.USER_UPDATE_ERROR))
                }
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun loginUser(
        email: String,
        password: String,
        role: UserRole,
        onSuccess: (User) -> Unit,
        onFailure: (String) -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { it ->
                    when (role) {
                        UserRole.PASSENGER -> {
                            PassengerManager.instance.getPassengerData(
                                it.user?.uid,
                                onSuccess = {
                                    try {
                                        val user = it
                                        user.userType = Config.ROLE_PASSENGER
                                        roomInstance.insertUser(user)
                                    } catch(e: Exception) {
                                        logoutUser(
                                            onSuccess = {},
                                            onFailure = {}
                                        )
                                        onFailure.invoke(Config.USER_UPDATE_ERROR)
                                    }
                                    onSuccess.invoke(it) },
                                onFailure = {
                                    logoutUser(
                                        onSuccess = {},
                                        onFailure = {}
                                    )
                                    onFailure.invoke(Config.ACCOUNT_BELONGS_TO_DRIVER) }
                            )
                        }
                        UserRole.DRIVER -> {
                            it.user?.uid?.let { it1 ->
                                DriverManager.instance.getDriverData(
                                    it1,
                                    onSuccess = {
                                        try {
                                            val user = it
                                            user.userType = Config.ROLE_DRIVER
                                            roomInstance.insertUser(user)
                                        } catch(e: Exception) {
                                            logoutUser(
                                                onSuccess = {},
                                                onFailure = {}
                                            )
                                            onFailure.invoke(Config.USER_UPDATE_ERROR)
                                        }
                                        onSuccess.invoke(it) },
                                    onFailure = {
                                        logoutUser(
                                            onSuccess = {},
                                            onFailure = {}
                                        )
                                        onFailure.invoke(Config.ACCOUNT_BELONGS_TO_PASSENGER) }
                                )
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    logoutUser(
                        onSuccess = {},
                        onFailure = {}
                    )
                    onFailure.invoke(Config.WRONG_CREDENTIALS)
                }
        } catch (e: Exception) {onFailure.invoke(Config.UNEXPECTED_ERROR)}
    }

    fun logoutUser(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        try {
            Log.d("myapp101", "logged in used: ${auth.currentUser?.email}")
            auth.signOut()
            try {
                roomInstance.clearRoomDB()
            } catch(e: Exception) {
                onFailure.invoke(java.lang.Exception(Config.USER_UPDATE_ERROR))
            }
            onSuccess.invoke()
        } catch (exception: Exception) {
            onFailure.invoke(exception)
        }
    }

    private fun addUserDataToFireStore(
        userType: String,
        userId: String,
        userData: User,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        fireStore.collection(userType)
            .document(userId)
            .set(userData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}