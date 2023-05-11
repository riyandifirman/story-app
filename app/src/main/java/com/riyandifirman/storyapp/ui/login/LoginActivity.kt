package com.riyandifirman.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.riyandifirman.storyapp.response.LoginResponse
import com.riyandifirman.storyapp.response.SignUpResponse
import com.riyandifirman.storyapp.settings.ApiConfig
import com.riyandifirman.storyapp.settings.Preferences
import com.riyandifirman.storyapp.ui.main.MainActivity
import com.riyandifirman.storyapp.ui.register.RegisterActivity
import kotlinx.coroutines.NonCancellable.start
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var myPreference: Preferences
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
        myPreference = Preferences(this)

        // jika sudah login, langsung pindah ke MainActivity
        if (myPreference.getStatusLogin()) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        showLoading(false)
        setMyButtonEnable()

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

        // listener untuk kolom password
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 8) setMyButtonEnable() else loginButton.isEnabled = false
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

        // fungsi ketika tombol login diklik
        loginButton.setOnClickListener{
            login(emailEditText.text.toString(), passwordEditText.text.toString())
            showLoading(true)
        }

        playAnimation()
    }

    // fungsi untuk mengatur button login
    private fun setMyButtonEnable() {
        val email = emailEditText.text
        val password = passwordEditText.text
        loginButton.isEnabled = (email != null && email.toString().isNotEmpty()) && (password != null && password.toString().isNotEmpty())
    }

    // fungsi untuk login
    private fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().loginUser(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            // Jika berhasil
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        if (loginResponse.error) {
                            Toast.makeText(this@LoginActivity, "Login Failed!", Toast.LENGTH_LONG).show()
                        } else {
                            myPreference.saveUserToken(loginResponse.loginResult.token)
                            myPreference.setStatusLogin(true)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            showLoading(false)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                }
            }

            // Jika gagal
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "onFailure: ${t.message.toString()}" , Toast.LENGTH_LONG).show()
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

        val loginNow = ObjectAnimator.ofFloat(binding.tvLoginNow, View.ALPHA, 1f).setDuration(500)
        val emailTitle = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTitle = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)
        val registerNow = ObjectAnimator.ofFloat(binding.tvRegisterNow, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(loginNow, emailTitle, email, passwordTitle, password, loginButton, register, registerNow)
            start()
        }
    }
}