package com.riyandifirman.storyapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.customview.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerButton: RegisterButton
    private lateinit var emailEditText: EditTextEmail
    private lateinit var passwordEditText: EditTextPassword
    private lateinit var errorPassword: TextView
    private lateinit var nameEditText: EditTextName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.btn_register)
        emailEditText = findViewById(R.id.ed_register_email)
        passwordEditText = findViewById(R.id.ed_register_password)
        errorPassword = findViewById(R.id.tv_error_password)
        nameEditText = findViewById(R.id.ed_register_name)

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

        nameEditText.addTextChangedListener(object : TextWatcher {
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
        val name = nameEditText.text
        registerButton.isEnabled = (email != null && email.toString().isNotEmpty()) && (password != null && password.toString().isNotEmpty()) && (name != null && name.toString().isNotEmpty())
    }
}