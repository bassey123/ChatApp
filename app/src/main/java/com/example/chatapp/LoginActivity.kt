package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Window
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        signup_link.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        login_reg.addTextChangedListener(object : TextWatcher {
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
        val regNo = login_reg.text.toString()
        val pwd = login_pwd.text.toString()

        if (TextUtils.isEmpty(regNo) || TextUtils.isEmpty(pwd)) {
            login_btn.setBackgroundResource(R.drawable.btn_disabled)
        } else {
            login_btn.setBackgroundResource(R.drawable.btn_enabled)
        }
    }
}
