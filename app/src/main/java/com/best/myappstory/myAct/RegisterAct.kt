package com.best.myappstory.myAct

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.best.myappstory.R
import com.best.myappstory.databinding.ActivityRegisterBinding
import com.best.myappstory.myViewModels.RegisterViewModel

class RegisterAct : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[RegisterViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edRegisterEmail.addTextChangedListener(object: TextWatcher {
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
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edRegisterPassword.alertCode == 1) {
                    binding.tvPasswordAlert.text = binding.edRegisterPassword.alertMsg
                    binding.tvPasswordAlert.visibility = TextView.VISIBLE
                } else {
                    binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.edRegisterPasswordConfirmation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (binding.edRegisterPasswordConfirmation.alertCode == 1) {
                    binding.tvPassword2Alert.text = binding.edRegisterPasswordConfirmation.alertMsg
                    binding.tvPassword2Alert.visibility = TextView.VISIBLE
                } else {
                    if (binding.edRegisterPassword.text.toString() != password) {
                        binding.tvPassword2Alert.text = resources.getString(R.string.pass_invalid_string)
                        binding.tvPassword2Alert.visibility = View.VISIBLE
                    } else {
                        binding.tvPassword2Alert.text = resources.getString(R.string.pass_valid_string)
                        binding.tvPassword2Alert.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val passwordConfirmation = binding.edRegisterPasswordConfirmation.text.toString()

            if (password == passwordConfirmation) {
                binding.tvPasswordAlert.visibility = TextView.INVISIBLE
                binding.tvPassword2Alert.visibility = TextView.INVISIBLE
                viewModel.registerUser(name, email, password)
                viewModel.isSuccess.observe(this) { isSuccess ->
                    showRegisterResponse(isSuccess)
                }
            }
        }
    }

    private fun showRegisterResponse(isSuccess: Boolean) {
        if (isSuccess) {
            binding.tvPassword2Alert.apply {
                visibility = View.VISIBLE
                setTextColor(Color.BLUE)
                text = resources.getString(R.string.reg_success_string)
            }
        } else {
            binding.tvPassword2Alert.apply {
                visibility = View.INVISIBLE
                setTextColor(Color.RED)
                text = resources.getString(R.string.reg_fail_string)
            }
        }
    }
}