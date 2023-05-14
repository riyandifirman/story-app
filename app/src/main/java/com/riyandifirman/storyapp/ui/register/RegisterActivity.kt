package com.riyandifirman.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.riyandifirman.storyapp.R
import com.riyandifirman.storyapp.customview.*
import com.riyandifirman.storyapp.databinding.ActivityRegisterBinding
import com.riyandifirman.storyapp.response.SignUpResponse
import com.riyandifirman.storyapp.settings.ApiConfig
import com.riyandifirman.storyapp.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: RegisterButton
    private lateinit var emailEditText: EditTextEmail
    private lateinit var passwordEditText: EditTextPassword
    private lateinit var errorPassword: TextView
    private lateinit var nameEditText: EditTextName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.btnRegister
        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword
        errorPassword = binding.errorPassword
        nameEditText = binding.edRegisterName

        setMyButtonEnable()
        showLoading(false)

        // listener untuk kolom email
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

        // listener untuk kolom nama
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

        // binding untuk textview error password
        passwordEditText.bindTextView(errorPassword)

        // listener untuk kolom password
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 8) setMyButtonEnable() else registerButton.isEnabled = false
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })

        // fungsi ketika tombol register ditekan
        registerButton.setOnClickListener {
            register(nameEditText.text.toString(), emailEditText.text.toString(), passwordEditText.text.toString())
            showLoading(true)
        }

        // fungsi ketika tulisan login here diklik
        binding.tvLoginHere.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        playAnimation()
    }

    // fungsi untuk mengatur button register
    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        val name = nameEditText.text
        registerButton.isEnabled = (email != null && email.toString().isNotEmpty()) && (password != null && password.toString().isNotEmpty()) && (name != null && name.toString().isNotEmpty())
    }

    // fungsi untuk melakukan register
    private fun register (name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().registerUser(name, email, password)
        client.enqueue(object : Callback<SignUpResponse> {
            // Jika berhasil
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        if (signUpResponse.error) {
                            Toast.makeText(this@RegisterActivity, signUpResponse.message, Toast.LENGTH_LONG).show()
                        } else {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            showLoading(false)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            // Jika gagal
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "onFailure: ${t.message.toString()}" , Toast.LENGTH_LONG).show()
            }
        })
    }

    // fungsi untuk menampilkan loading
    private fun showLoading(state: Boolean) { binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        val registerNow = ObjectAnimator.ofFloat(binding.tvRegisterNow, View.ALPHA, 1f).setDuration(500)
        val nameTitle = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailTitle = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTitle = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val loginHere = ObjectAnimator.ofFloat(binding.tvLoginHere, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(registerNow, nameTitle, name, emailTitle, email, passwordTitle, password, registerButton, login, loginHere)
            start()
        }
    }
}