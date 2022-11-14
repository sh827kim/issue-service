package com.spark.userservice.exception

import java.lang.RuntimeException

sealed class ServerException(
    val code: Int,
    override val message: String,
) : RuntimeException()

data class UserNotFoundException (
    override val message: String = "존재하지 않는 유저입니다.",
): ServerException(404, message)

data class PasswordNotVerifiedException (
    override val message: String = "패스워드가 잘못되었습니다.",
): ServerException(400, message)

data class UserExistsException (
    override val message: String = "이미 존재하는 유저입니다.",
) : ServerException(409, message)

data class InvalidJwtTokenException (
    override val message: String = "잘못된 토큰입니다..",
) : ServerException(409, message)