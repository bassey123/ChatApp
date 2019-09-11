package com.example.chatapp

import android.os.Bundle
import android.view.Window
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.chatapp.fragments.FriendsFragment
import com.example.chatapp.fragments.MessagesFragment
import com.example.chatapp.fragments.ProfileFragment

class HomeActivity : AppCompatActivity() {

    private val fragment1: Fragment = MessagesFragment.newInstance()
    private val fragment2: Fragment = FriendsFragment.newInstance()
    private val fragment3: Fragment = ProfileFragment.newInstance()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragment1

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
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_home)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.fragmentContainer, fragment3, "3").hide(fragment3).commit()
        fm.beginTransaction().add(R.id.fragmentContainer, fragment2, "2").hide(fragment2).commit()
        fm.beginTransaction().add(R.id.fragmentContainer, fragment1, "1").commit()
    }
}
