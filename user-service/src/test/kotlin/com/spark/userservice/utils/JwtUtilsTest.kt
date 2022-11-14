package com.spark.userservice.utils

import com.spark.userservice.config.JwtProperties
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class JwtUtilsTest {

    private val log = KotlinLogging.logger {}

    @Test
    fun createTokenTest() {
        val jwtClaim = getJwtClaimStub()
        val jwtProperties = getJwtProperties()
        val token = JwtUtils.createToken(jwtClaim, jwtProperties)

        assertNotNull(token)

        log.info { "token : $token" }
    }

    @Test
    fun decodeTest() {
        val jwtClaim = getJwtClaimStub()
        val jwtProperties = getJwtProperties()
        val token = JwtUtils.createToken(jwtClaim, jwtProperties)

        assertNotNull(token)

        val decode = JwtUtils.decode(token = token, secret = jwtProperties.secret, issuer = jwtProperties.issuer)

        with(decode) {
            log.info { "claims : $claims" }

            val userId = claims["userId"]!!.asLong()
            assertEquals(jwtClaim.userId, userId)

            val email = claims["email"]!!.asString()
            assertEquals(jwtClaim.email, email)

            val profileUrl = claims["profileUrl"]!!.asString()
            assertEquals(jwtClaim.profileUrl, profileUrl)

            val username = claims["username"]!!.asString()
            assertEquals(jwtClaim.username, username)
        }
    }

    private fun getJwtClaimStub() = JwtClaim(
        userId = 1,
        email = "dev@gmail.com",
        profileUrl = "profile.jpg",
        username = "김개발",
    )

    private fun getJwtProperties() = JwtProperties(
        issuer = "jara",
        subject = "auth",
        expiresTime = 3600,
        secret = "spark-secret"
    )
}