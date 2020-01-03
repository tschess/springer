package io.bahlsenwitz.springer.request.player

import javax.validation.constraints.NotBlank

data class RequestLogin (
    @NotBlank(message = "username")
    var name: String,
    @NotBlank(message = "password")
    var password: String,
    val device: String,
    val updated: String,
    val api: String
)