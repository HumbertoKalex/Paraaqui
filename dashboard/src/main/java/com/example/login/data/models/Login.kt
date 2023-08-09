package com.example.login.data.models

import com.google.gson.annotations.SerializedName


data class Login(
    @SerializedName("user") var user: String,
    @SerializedName("password") var password: String
) : java.io.Serializable


