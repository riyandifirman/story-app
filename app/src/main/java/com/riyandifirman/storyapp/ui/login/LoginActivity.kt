package com.riyandifirman.storyapp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.customview.EditTextEmail
import com.riyandifirman.storyapp.customview.EditTextPassword
import com.riyandifirman.storyapp.customview.LoginButton

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EditTextEmail
    private lateinit var passwordEditText: EditTextPassword
    private lateinit var errorPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.btn_login)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.ed_login_password)
        errorPassword = findViewById(R.id.tv_error_password)

        setMyButtonEnable()

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
                if (s != null && s.length < 8) {
                    errorPassword.visibility = View.VISIBLE
                } else {
                    errorPassword.visibility = View.GONE
                }
            }
        })
    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString().isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }
}