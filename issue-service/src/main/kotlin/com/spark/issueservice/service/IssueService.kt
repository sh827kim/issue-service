package com.spark.issueservice.service

import com.spark.issueservice.domain.CommentRepository
import com.spark.issueservice.domain.Issue
import com.spark.issueservice.domain.IssueRepository
import com.spark.issueservice.domain.enums.IssueStatus
import com.spark.issueservice.dto.IssueRequest
import com.spark.issueservice.dto.IssueResponse
import com.spark.issueservice.exception.NotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IssueService (
    private val issueRepository: IssueRepository,
    private val commentRepository: CommentRepository,
) {

    @Transactional
    fun create(userId : Long, request: IssueRequest) : IssueResponse {
        val issue = Issue(
            summary = request.summary,
            description = request.description,
            userId = userId,
            type = request.type,
            priority = request.priority,
            status = request.status,
        )

        return IssueResponse(issueRepository.save(issue))
    }

    @Transactional(readOnly = true)
    fun getAll(status: IssueStatus) =
        issueRepository.findAllByStatusOrderByCreatedAtDesc(status)
            ?.map { IssueResponse(it) }

    @Transactional(readOnly = true)
    fun getDetail(issueId : Long) : IssueResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
        return IssueResponse(issue)
    }

    @Transactional
    fun update(issueId: Long, issueRequest: IssueRequest) : IssueResponse {
        val issue = issueRepository.findByIdOrNull(issueId) ?: throw NotFoundException("이슈가 존재하지 않습니다.")
        return with(issue) {
            summary = issueRequest.summary
            description = issueRequest.description
            type = issueRequest.type
            priority = issueRequest.priority
            status= issueRequest.status
            IssueResponse(issueRepository.save(this))
        }
    }

    @Transactional
    fun delete(issueId: Long) {
        issueRepository.deleteById(issueId)
    }
}