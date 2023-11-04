package com.best.myappstory.myViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.myappstory.myApi.ApiConfig
import com.best.myappstory.myResponse.RegisterRequest
import com.best.myappstory.myResponse.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {
    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun registerUser(name: String, email: String, password: String) {
        val regRequest = RegisterRequest(name, email, password)
        val call = ApiConfig.getApiService("").register(regRequest)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isSuccess.value = true
                        Log.d(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    _isSuccess.value = false
                    Log.e(TAG, "onResponse: ${response.body()?.message ?: "Register failed"}")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isSuccess.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun isEmailValid(email: String): Boolean {
        val emailPattern1 = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return email.matches(emailPattern1)
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}