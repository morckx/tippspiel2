package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.core.properties.AuthenticationProperties
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.Standing
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.persistence.repository.RoleRepository
import com.github.zemke.tippspiel2.persistence.repository.StandingRepository
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
        @Autowired private var userRepository: UserRepository,
        @Autowired private var roleRepository: RoleRepository,
        @Autowired private var standingRepository: StandingRepository,
        @Autowired private val authenticationProperties: AuthenticationProperties,
        @Autowired private val authenticationManager: AuthenticationManager,
        @Autowired private val jsonWebTokenService: JsonWebTokenService,
        @Autowired private val userDetailsService: UserDetailsService
) {

    @Transactional
    fun addUser(firstName: String, lastName: String, email: String, plainPassword: String,
                bettingGame: BettingGame, roles: List<UserRole> = listOf(UserRole.ROLE_USER)): User {
        val persistedUser = userRepository.save(User(
                FullName(firstName, lastName), email,
                BCrypt.hashpw(plainPassword, authenticationProperties.bcryptSalt),
                roleRepository.findByNameIn(roles), listOf(bettingGame)))

        standingRepository.save(Standing(
                id = null,
                points = 0,
                exactBets = 0,
                goalDifferenceBets = 0,
                winnerBets = 0,
                wrongBets = 0,
                missedBets = 0,
                user = persistedUser,
                bettingGame = bettingGame
        ))

        return persistedUser
    }

    fun findUser(id: Long): Optional<User> {
        return userRepository.findById(id)
    }

    fun findUsers(users: List<Long>): List<User> {
        return userRepository.findAllById(users)
    }

    fun findUserByEmail(email: String): User? = userRepository.findByEmail(email)

    @Throws(AuthenticationException::class)
    fun authenticate(email: String, password: String): String {
        SecurityContextHolder.getContext().authentication =
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(email, password))
        return jsonWebTokenService.generateToken(
                userDetailsService.loadUserByUsername(email) as AuthenticatedUser)
    }

    fun findAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun updateUser(updatedUser: User, newPlainPassword: String?): User {
        if (newPlainPassword != null)
            updatedUser.password = BCrypt.hashpw(newPlainPassword, authenticationProperties.bcryptSalt)
        return userRepository.save(updatedUser)
    }
}
