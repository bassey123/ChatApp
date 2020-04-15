package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        forgot_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ResetPasswordActivity::class.java))
        }

        login_btn.setOnClickListener {

            val email = login_email.text.toString()
            val pwd = login_pwd.text.toString()

            login_pb.visibility = View.VISIBLE

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                login_pb.visibility = View.GONE
            } else {
                auth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener {
                        if(it.isSuccessful) {
                            login_pb.visibility = View.GONE
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        signup_link.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        login_email.addTextChangedListener(object : TextWatcher {
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

        login_pwd.addTextChangedListener(object : TextWatcher {
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
    }

    private  fun btnColor() {
        val email = login_email.text.toString()
        val pwd = login_pwd.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            login_btn.setBackgroundResource(R.drawable.btn_disabled)
        } else {
            login_btn.setBackgroundResource(R.drawable.btn_enabled)
        }
    }
}
