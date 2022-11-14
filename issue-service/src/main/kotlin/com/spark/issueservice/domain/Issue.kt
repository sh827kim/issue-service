package com.spark.issueservice.domain

import com.spark.issueservice.domain.enums.IssuePriority
import com.spark.issueservice.domain.enums.IssueStatus
import com.spark.issueservice.domain.enums.IssueType
import javax.persistence.*


@Entity
@Table
class Issue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column
    var userId: Long,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments : MutableList<Comment> = mutableListOf(),

    @Column
    var summary : String,

    @Column
    var description : String,

    @Column
    @Enumerated(EnumType.STRING)
    var type : IssueType,

    @Column
    @Enumerated(EnumType.STRING)
    var priority: IssuePriority,

    @Column
    @Enumerated(EnumType.STRING)
    var status : IssueStatus,

) : BaseEntity()