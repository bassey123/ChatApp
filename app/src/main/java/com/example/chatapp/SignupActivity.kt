package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Window
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_signup)

        signup_btn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        login_link.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signup_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btnColor()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnColor()
            }
        })

        signup_pwd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnColor()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btnColor()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnColor()
            }
        })

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
    }

    private  fun btnColor() {
        val userName = signup_user.text.toString()
        val regNo = signup_reg.text.toString()
        val email = signup_email.text.toString()
        val pwd = signup_pwd.text.toString()

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(regNo) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            signup_btn.setBackgroundResource(R.drawable.btn_disabled)
        } else {
            signup_btn.setBackgroundResource(R.drawable.btn_enabled)
        }
    }


    fun register(username: String, reg_no: String, email: String, password: String) {
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> {

            })
    }
}
