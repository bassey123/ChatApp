package com.example.chatapp

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.chatapp.fragments.UsersFragment
import com.example.chatapp.fragments.MessagesFragment
import com.example.chatapp.fragments.ProfileFragment
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val fragment1: Fragment = MessagesFragment.newInstance()
    private val fragment2: Fragment = UsersFragment.newInstance()
    private val fragment3: Fragment = ProfileFragment.newInstance()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragment1

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_messages -> {
                fm.beginTransaction().hide(active).show(fragment1).commit()
                active = fragment1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_friends -> {
                fm.beginTransaction().hide(active).show(fragment2).commit()
                active = fragment2
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                fm.beginTransaction().hide(active).show(fragment3).commit()
                active = fragment3
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(home_toolbar)
        supportActionBar!!.title = ""


        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        /*reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
                var unread = 0
                for (snapshot: DataSnapshot in p0.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.receiver == firebaseUser.uid && chat.isseen) {
                        unread++
                    }
                }

                if (unread == 0) {
                    navView.menu.findItem(R.id.navigation_messages).title = "Messages"
                } else {
                    navView.menu.findItem(R.id.navigation_messages).title = "Messages($unread)"
                }
            }
        })*/


        fm.beginTransaction().add(R.id.fragmentContainer, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.fragmentContainer, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.fragmentContainer, fragment1, "1").commit()

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val user: User = p0.getValue(User::class.java)!!
                home_username.text = user.userName
                if (user.imageURL == "default") {
                    home_profileImage.setImageResource(R.drawable.ic_person)
                } else {
                    Glide.with(applicationContext)
                        .load(user.imageURL)
                        .into(home_profileImage)
                }
            }
        })
    }

    private fun status(status: String) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["status"] = status

        reference.updateChildren(hashMap)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }
}
