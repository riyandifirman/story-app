package com.riyandifirman.storyapp.ui.login

import android.content.Intent
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
import com.riyandifirman.storyapp.databinding.ActivityLoginBinding
import com.riyandifirman.storyapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EditTextEmail
    private lateinit var passwordEditText: EditTextPassword
    private lateinit var errorPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginButton = binding.btnLogin
        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
        errorPassword = binding.tvErrorPassword

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

        // fungsi ketika tulisan register now diklik
        binding.tvRegisterNow.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString().isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }
}