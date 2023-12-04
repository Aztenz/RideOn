package com.example.rideon.model.database


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserManager private constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    companion object {
        @Volatile
        private var instance: UserManager? = null

        fun getInstance(): UserManager {
            return instance ?: synchronized(this) {
                instance ?: UserManager().also { instance = it }
            }
        }
    }

    fun registerUser(email: String, password: String, displayName: String, onSuccess:
        (uid: String) -> Unit, onFailure: (Exception) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                linkAuthUidToFireStoreDocument(
                    user?.uid,
                    displayName,
                    email,
                    onSuccess,
                    onFailure)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun loginUser(email: String, password: String, onSuccess: (uid: String) -> Unit, onFailure:
        (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                linkAuthUidToFireStoreDocument(
                    user?.uid,
                    user?.displayName.toString(),
                    user?.email.toString(),
                    onSuccess,
                    onFailure)
            }
            .addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getUserData(uid: String, callback: (Map<String, Any>?) -> Unit) {
        val userRef = fireStore.collection("users").document(uid)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    callback(documentSnapshot.data)
                } else {
                    // Document does not exist
                    callback(null)
                }
            }
            .addOnFailureListener {
                // Handle error
                callback(null)
            }
    }

    private fun linkAuthUidToFireStoreDocument(
        uid: String?,
        displayName: String,
        email: String,
        onSuccess: (uid: String) -> Unit,
        onFailure: (Exception) -> Unit) {
        uid?.let {
            val userDocument = fireStore.collection("users").document(uid)
            val userData = hashMapOf(
                "email" to email,
                "displayName" to displayName
                // Add other user data as needed
            )

            userDocument.set(userData)
                .addOnSuccessListener {
                    onSuccess.invoke(uid)
                }
                .addOnFailureListener { exception ->
                    onFailure.invoke(exception)
                }
        }
    }
}
