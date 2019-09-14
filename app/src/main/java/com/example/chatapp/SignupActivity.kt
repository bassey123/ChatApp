package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Window
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        signup_btn.setOnClickListener {

            val userName = signup_user.text.toString()
            val regNo = signup_reg.text.toString()
            val email = signup_email.text.toString()
            val pwd = signup_pwd.text.toString()

            if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(regNo) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else if(pwd.length < 6) {
                Toast.makeText(this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show()
            } else {
            registerUser(userName, regNo, email, pwd)
            }
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


    private fun registerUser(username: String, reg_no: String, email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser!!.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap = HashMap<String, String>()
                    hashMap["id"] = userId
                    hashMap["userName"] = username
                    hashMap["regNo"] = reg_no
                    hashMap["email"] = email
                    hashMap["imageURL"] = "default"
                    hashMap["status"] = "offline"
                    hashMap["search"] = username.toLowerCase()

                    reference.setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    Toast.makeText(this, "You can't register with email or password", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
