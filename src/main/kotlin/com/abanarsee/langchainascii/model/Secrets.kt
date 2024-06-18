package com.abanarsee.shared.model

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class Secrets(
    @SerialName("chatgpt")
    val chatgpt: String,
)