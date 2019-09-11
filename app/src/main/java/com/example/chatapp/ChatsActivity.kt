package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chats.*
import kotlin.collections.HashMap

class ChatsActivity : AppCompatActivity() {

    private lateinit var fuser: FirebaseUser
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val intent = intent
        val userid = intent.getStringExtra("userid")
        fuser = FirebaseAuth.getInstance().currentUser!!

        send_btn.setOnClickListener {
            val msg = send_text.text.toString()
            if (msg != "") {
                sendMessage(fuser.uid, userid, msg)
            } else {
                Toast.makeText(this@ChatsActivity, "You can't send empty message", Toast.LENGTH_SHORT).show()
            }
            send_text.setText("")
        }

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user: User = p0.getValue(User::class.java)!!
                chat_username.text = user.userName
                if (user.imageURL == "default") {
                    chat_profile_image.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(this@ChatsActivity)
                        .load(user.imageURL)
                        .into(chat_profile_image)
                }
            }
        })
    }

    private fun sendMessage(sender: String, receiver: String, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message

        reference.child("Chats").push().setValue(hashMap)
    }
}
