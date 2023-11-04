package com.best.myappstory.myAct

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import com.best.myappstory.R
import com.best.myappstory.databinding.ActivityLoginBinding
import com.best.myappstory.myViewModels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (viewModel.isEmailValid(email)) {
                    binding.tvEmailAlert.text = ""
                } else {
                    binding.tvEmailAlert.text = resources.getString(R.string.email_invalid_string)
                }
            }

        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edLoginPassword.alertCode == 1) {
                    binding.tvPasswordAlert.text = binding.edLoginPassword.alertMsg
                    binding.tvPasswordAlert.visibility = TextView.VISIBLE
                } else {
                    binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.loginUser(email, password)
            viewModel.isLoading.observe(this) {isLoading -> showLoading(isLoading)}
            viewModel.isSuccess.observe(this) {isSuccess -> showLoginResponse(isSuccess)}
            viewModel.token.observe(this) {token -> saveSession(token)}
        }

        binding.btnCreateAccount.setOnClickListener {
            navigateToRegister()
        }

        val sharedPreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            toMainActivity()
        }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterAct::class.java)
        val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this@LoginActivity as Activity,
            Pair(binding.ivPhoto, "profile"),
            Pair(binding.edLoginEmail, "email"),
            Pair(binding.edLoginPassword, "password"),
            Pair(binding.btnCreateAccount, "regbutton"),
        )
        this.startActivity(intent, optionsCompat.toBundle())
    }

    private fun saveSession(token: String?) {
        val sharedPreferences = getSharedPreferences("LoginSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("token", token)
        editor.apply()
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            ObjectAnimator.ofFloat(binding.lyLoginInfo, View.ALPHA, 0.3f).start()
            binding.loadLogin.visibility = View.VISIBLE
        } else {
            ObjectAnimator.ofFloat(binding.lyLoginInfo, View.ALPHA, 1f).start()
            binding.loadLogin.visibility = View.GONE
        }
    }
    private fun showLoginResponse(isSuccess: Boolean) {
        if (isSuccess) {
            Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
            binding.tvPasswordAlert.visibility = View.INVISIBLE
            toMainActivity()
        } else {
            binding.tvPasswordAlert.visibility = View.VISIBLE
            binding.tvPasswordAlert.text = resources.getString(R.string.log_fail_string)
        }
    }
}