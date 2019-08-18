package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_main)

        login_btn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signup_btn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
