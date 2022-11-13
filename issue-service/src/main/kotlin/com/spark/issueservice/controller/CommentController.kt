package com.spark.issueservice.controller

import com.spark.issueservice.config.AuthUser
import com.spark.issueservice.dto.CommentRequest
import com.spark.issueservice.dto.CommentResponse
import com.spark.issueservice.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/issues/{issueId}/comments")
@RestController
class CommentController (
    private val commentService: CommentService,
){
    @PostMapping
    fun createComment(
        authUser: AuthUser,
        @PathVariable issueId : Long,
        @RequestBody commentRequest: CommentRequest
    ) : CommentResponse {
        return with(authUser) {
            commentService.createComment(issueId, userId, username, commentRequest)
        }
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        authUser: AuthUser,
        @PathVariable commentId : Long,
        @RequestBody commentRequest: CommentRequest,
    ) = commentService.updateComment(authUser.userId, commentId, commentRequest)


    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteComment(
        authUser: AuthUser,
        @PathVariable issueId: Long,
        @PathVariable commentId: Long,
    ) {
        commentService.deleteComment(issueId, commentId, authUser.userId)
    }
}