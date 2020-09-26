package com.example.mvi_demo.model

import com.squareup.moshi.Json

data class User(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "email")
    val email: String? = null,
    @Json(name = "avatar")
    val avatar: String? = null
)