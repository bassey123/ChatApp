package com.example.chatapp.notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val refreshToken: String = FirebaseInstanceId.getInstance().token.toString()
        if (firebaseUser != null) {
            updateToken(refreshToken)
        }
    }

    private fun updateToken(refreshToken: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Tokens")
        val token = Token(refreshToken)
        reference.child(firebaseUser!!.uid).setValue(token)
    }
}
