package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.adapter.ChatAdapter
import com.example.chatapp.model.Chat
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chats.*
import kotlin.collections.HashMap

class ChatsActivity : AppCompatActivity() {

    private lateinit var fuser: FirebaseUser
    private lateinit var reference: DatabaseReference

    lateinit var chatAdapter: ChatAdapter
    val mChat = ArrayList<Chat>()

    lateinit var recyclerView: RecyclerView

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

        recyclerView = findViewById(R.id.chats_recyclerView)
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

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

                readMessages(fuser.uid, userid, user.imageURL)
            }
        })
    }

    private fun sendMessage(sender: String, receiver: String, message: String) {

        val reference = FirebaseDatabase.getInstance().reference
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message

        reference.child("Chats").push().setValue(hashMap)
    }

    private fun readMessages(myid: String, userid: String, imageurl: String) {

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                mChat.clear()
                for (snapshot: DataSnapshot in p0.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.receiver == myid && chat.sender == userid ||
                        chat.receiver == userid && chat.sender == myid) {
                        mChat.add(chat)
                    }
                    chatAdapter = ChatAdapter(this@ChatsActivity, mChat, imageurl)
                    recyclerView.adapter = chatAdapter
                }
            }
        })
    }
}
