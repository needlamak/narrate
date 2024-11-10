package com.example.narrate

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthManager(private val activity: MainActivity) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val sessionManager = SessionManager(activity)

    // Function to upload user data including username, date, and image
    fun uploadUserData(username: String, date: String, imageResId: Int, onComplete: (Boolean, String?) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userData = hashMapOf(
                "username" to username,
                "date" to date,
                "imageResId" to imageResId // Store the image resource ID
            )

            firestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    // Save session after successful upload
                    sessionManager.saveUserSession(username, date, imageResId)
                    onComplete(true, null) // Upload successful
                }
                .addOnFailureListener { exception ->
                    onComplete(false, exception.message) // Handle error
                }
        } else {
            onComplete(false, "User not logged in.")
        }
    }

    fun fetchUserData(onComplete: (String?, String?, Int?) -> Unit) {
        val userSession = sessionManager.getUserSession()

        if (userSession != null) {
            // Return the data from the session if it exists
            onComplete(userSession.username, userSession.date, userSession.imageResId)
        } else {
            // Fetch from Firestore if no session exists
            val userId = auth.currentUser?.uid
            userId?.let {
                firestore.collection("users").document(it).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            val username = document.getString("username")
                            val date = document.getString("date")
                            val imageResId = document.getLong("imageResId")?.toInt()

                            // Save to session for future use
                            username?.let { name ->
                                date?.let { d ->
                                    imageResId?.let { id ->
                                        sessionManager.saveUserSession(name, d, id)
                                    }
                                }
                            }

                            onComplete(username, date, imageResId)
                        } else {
                            onComplete(null, null, null) // No data found
                        }
                    }
                    .addOnFailureListener { onComplete(null, null, null) }
            } ?: run {
                onComplete(null, null, null) // No user logged in
            }
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

    fun signOut() {
        auth.signOut()
        sessionManager.clearSession() // Clear session data
    }
}
//class AuthManager(private val activity: MainActivity) {
//    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//    // Function to upload user data including username, date, and image
//    fun uploadUserData(username: String, date: String, imageResId: Int, onComplete: (Boolean, String?) -> Unit) {
//        val userId = auth.currentUser?.uid
//        if (userId != null) {
//            val userData = hashMapOf(
//                "username" to username,
//                "date" to date,
//                "imageResId" to imageResId // Store the image resource ID as a string or integer
//            )
//
//            firestore.collection("users").document(userId).set(userData)
//                .addOnSuccessListener {
//                    onComplete(true, null) // Upload successful
//                }
//                .addOnFailureListener { exception ->
//                    onComplete(false, exception.message) // Handle error
//                }
//        } else {
//            onComplete(false, "User not logged in.")
//        }
//    }
//
//    fun signUp(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val userId = auth.currentUser?.uid
//                    val userData = hashMapOf("email" to email)
//
//                    userId?.let {
//                        firestore.collection("users").document(it).set(userData)
//                            .addOnSuccessListener { onComplete(true, null) }
//                            .addOnFailureListener { onComplete(false, it.message) }
//                    }
//                } else {
//                    onComplete(false, task.exception?.message)
//                }
//            }
//    }
//
//    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                onComplete(task.isSuccessful, task.exception?.message)
//            }
//    }
//
//    // Function to fetch user data including username, date and image
//    fun fetchUserData(onComplete: (String?, String?, Int?) -> Unit) {
//        val userId = auth.currentUser?.uid
//        userId?.let {
//            firestore.collection("users").document(it).get()
//                .addOnSuccessListener { document ->
//                    if (document != null) {
//                        val username = document.getString("username")
//                        val date = document.getString("date") // Assuming date is stored as a string
//                        val imageResId = document.getLong("imageResId")?.toInt() // Convert Long to Int if necessary
//
//                        onComplete(username, date, imageResId)
//                    } else {
//                        onComplete(null, null, null) // No data found
//                    }
//                }
//                .addOnFailureListener { onComplete(null, null, null) }
//        } ?: run {
//            onComplete(null, null, null) // No user logged in
//        }
//    }
//    // Sign Out
//    fun signOut() {
//        auth.signOut()
//    }
//}