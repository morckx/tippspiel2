package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class UserDto(

        val id: Long,
        val firstName: String,
        val lastName: String,
        val email: String,
        val token: String?
) {
    companion object {

        fun toDto(user: User): UserDto =
                UserDto(user.id!!, user.fullName.firstName, user.fullName.lastName, user.email, null)

        fun toDto(user: User, token: String): UserDto =
                UserDto(user.id!!, user.fullName.firstName, user.fullName.lastName, user.email, token)
    }
}
