package com.spark.userservice.controller

import com.spark.userservice.dto.*
import com.spark.userservice.service.UserService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import mu.KotlinLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
@RequestMapping("/api/users")
class UserController (
    private val userService: UserService,
        ){

    private val log = KotlinLogging.logger {  }

    @PostMapping("/signup")
    suspend fun signUp(@RequestBody request: SignUpRequest) {
        userService.signUp(request)
    }

    @PostMapping("/signin")
    suspend fun signIn(@RequestBody signInRequest: SignInRequest) : SignInResponse {
        return userService.signIn(signInRequest)
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun logout(@AuthToken token : String) {
        userService.logout(token)
    }

    @GetMapping("/me")
    suspend fun getMyInfo(@AuthToken token: String) : UserResponse {
        return UserResponse(userService.getMyInfo(token))
    }

    @GetMapping("/{userId}/username")
    suspend fun getReporterInfo(@PathVariable userId: Long) : Map<String, String> {
        return mapOf("reporter" to userService.getUser(userId).username)
    }

    @PostMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun edit(
        @PathVariable id: Long,
        @ModelAttribute request: UserEditRequest,
        @AuthToken token: String,
        @RequestPart("profileUrl") filePart: FilePart,
    ) {
        val originalFileName = filePart.filename()
        var filename: String? = null


        if(originalFileName.isNotEmpty()) {
            val extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1)
            filename = "${id}.${extension}"
            val file = File(ClassPathResource("/images/").file, filename)
            filePart.transferTo(file).awaitSingleOrNull()
        }

        userService.edit(token, request.username, filename)
    }

}