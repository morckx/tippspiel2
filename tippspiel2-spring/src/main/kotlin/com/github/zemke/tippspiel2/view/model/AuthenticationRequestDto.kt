package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class AuthenticationRequestDto(

        val username: String,
        val password: String
)