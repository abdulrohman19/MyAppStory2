package com.best.myappstory.myResponse

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)