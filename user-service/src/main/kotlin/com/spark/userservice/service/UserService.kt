package com.spark.userservice.service

import com.auth0.jwt.interfaces.DecodedJWT
import com.spark.userservice.config.CoroutineCacheManager
import com.spark.userservice.config.JwtProperties
import com.spark.userservice.domain.entity.User
import com.spark.userservice.domain.repository.UserRepository
import com.spark.userservice.dto.SignInRequest
import com.spark.userservice.dto.SignInResponse
import com.spark.userservice.dto.SignUpRequest
import com.spark.userservice.exception.InvalidJwtTokenException
import com.spark.userservice.exception.PasswordNotVerifiedException
import com.spark.userservice.exception.UserExistsException
import com.spark.userservice.exception.UserNotFoundException
import com.spark.userservice.utils.BCryptUtils
import com.spark.userservice.utils.JwtClaim
import com.spark.userservice.utils.JwtUtils
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration


@Service
class UserService (
    private val userRepository: UserRepository,
    private val jwtProperties: JwtProperties,
    private val cacheManager: CoroutineCacheManager<User>
) {
    val log = KotlinLogging.logger {  }

    companion object {
        private val CACHE_TTL = Duration.ofMinutes(1)
    }

    suspend fun signUp(signUpRequest: SignUpRequest) {
        with(signUpRequest) {
            userRepository.findByEmail(email)?.let {
                throw UserExistsException()
            }

            val user = User(
                email = email,
                password = BCryptUtils.hash(password),
                username = username,
            )
            val savedUser = userRepository.save(user)

            log.info { "Saved User : $savedUser" }
            savedUser
        }
    }
    suspend fun signIn(signInRequest: SignInRequest) : SignInResponse {
        return with(userRepository.findByEmail(signInRequest.email) ?: throw UserNotFoundException()) {
            val verified = BCryptUtils.verify(signInRequest.password, password)

            if(!verified) {
                throw PasswordNotVerifiedException()
            }

            val jwtClaim = JwtClaim(
                userId = id!!,
                email = email,
                profileUrl = profileUrl,
                username = username
            )

            val token = JwtUtils.createToken(jwtClaim, jwtProperties)

            cacheManager.awaitPut(key = token, value = this, ttl = CACHE_TTL)
            SignInResponse(
                email = email,
                username = username,
                token = token
            )
        }
    }

    suspend fun logout(token: String) {
        cacheManager.awaitEvict(token)
    }

    suspend fun getMyInfo(token: String) :User {
        return cacheManager.awaitGetOrPut(key = token, ttl= CACHE_TTL) {
            val decodedJwt : DecodedJWT = JwtUtils.decode(token, jwtProperties.secret, jwtProperties.issuer)

            val userId = decodedJwt.claims["userId"]?.asLong() ?: throw InvalidJwtTokenException()

            getUser(userId)
        }
    }

    suspend fun getUser(userId: Long) : User {
        return userRepository.findById(userId) ?: throw UserNotFoundException()
    }

    suspend fun edit(token: String, username: String, filename: String?) : User {
        val user = getMyInfo(token)

        val newUser = user.copy(username = username, profileUrl = filename ?: user.profileUrl)

        return userRepository.save(newUser).also {
            cacheManager.awaitPut(token, it, CACHE_TTL)
        }

    }
}