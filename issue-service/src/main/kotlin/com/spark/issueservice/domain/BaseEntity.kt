package com.spark.issueservice.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners
abstract class BaseEntity (
    @CreatedDate
    var createdAt : LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt : LocalDateTime? = null,
)