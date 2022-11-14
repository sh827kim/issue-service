package com.spark.issueservice.controller

import com.spark.issueservice.config.AuthUser
import com.spark.issueservice.domain.enums.IssueStatus
import com.spark.issueservice.dto.IssueRequest
import com.spark.issueservice.service.IssueService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/issues")
@RestController
class IssueController(
    private val issueService: IssueService,
) {

    @PostMapping
    fun create(
        authUser: AuthUser,
        @RequestBody request: IssueRequest,
    ) = issueService.create(authUser.id, request)

    @GetMapping
    fun getAll(
        authUser: AuthUser,
        @RequestParam(required = false, defaultValue = "TODO") status : IssueStatus,
    ) = issueService.getAll(status)

    @GetMapping("/{id}")
    fun getDetail(
        authUser: AuthUser,
        @PathVariable id : Long,
    ) = issueService.getDetail(id)

    @PutMapping("/{id}")
    fun update(
        authUser: AuthUser,
        @PathVariable id: Long,
        @RequestBody issueRequest: IssueRequest,
    ) = issueService.update(id, issueRequest)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        authUser: AuthUser,
        @PathVariable id : Long,
    ) {
        issueService.delete(id)
    }
}