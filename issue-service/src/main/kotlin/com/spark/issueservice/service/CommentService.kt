package com.spark.issueservice.service

import com.spark.issueservice.domain.Comment
import com.spark.issueservice.domain.CommentRepository
import com.spark.issueservice.domain.IssueRepository
import com.spark.issueservice.dto.CommentRequest
import com.spark.issueservice.dto.CommentResponse
import com.spark.issueservice.dto.toResponse
import com.spark.issueservice.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val issueRepository: IssueRepository,
) {

    @Transactional
    fun createComment(
        issueId : Long,
        userId : Long,
        username : String,
        request: CommentRequest
    ) : CommentResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")

        val comment = Comment(
            issue = issue,
            userId = userId,
            username = username,
            body = request.body,
        )
        issue.comments.add(comment)
        return commentRepository.save(comment).toResponse()
    }

    @Transactional
    fun updateComment(
        userId: Long,
        commentId: Long,
        request: CommentRequest,
    ) : CommentResponse? {
        return commentRepository.findByIdAndUserId(commentId, userId)?.run {
            body = request.body
            commentRepository.save(this).toResponse()
        }
    }

    @Transactional
    fun deleteComment(
        issueId: Long,
        commentId: Long,
        userId: Long,
    ) {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")

        commentRepository.findByIdAndUserId(commentId, userId).let { comment ->
            issue.comments.remove(comment)
        }
    }
}