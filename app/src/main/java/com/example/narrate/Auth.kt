package com.example.narrate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthManager(private val activity: MainActivity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun uploadUserName(name: String, onComplete: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userData = hashMapOf("username" to name)

            firestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    onComplete(true, null) // Upload successful
                }
                .addOnFailureListener { exception ->
                    onComplete(false, exception.message) // Handle error
                }
        } else {
            onComplete(false, "User not logged in.")
        }
    }

    fun signUp(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    val userData = hashMapOf("email" to email)

                    userId?.let {
                        firestore.collection("users").document(it).set(userData)
                            .addOnSuccessListener { onComplete(true, null) }
                            .addOnFailureListener { onComplete(false, it.message) }
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful, task.exception?.message)
            }
    }

    fun fetchUserData(onComplete: (String?) -> Unit) {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val username =
                            document.getString("username") // Assuming you store the username here
                        onComplete(username)
                    } else {
                        onComplete(null) // No data found
                    }
                }
                .addOnFailureListener { onComplete(null) }
        } ?: run {
            onComplete(null) // No user logged in
        }
    }

    // Sign Out
    fun signOut() {
        auth.signOut()
    }
}
