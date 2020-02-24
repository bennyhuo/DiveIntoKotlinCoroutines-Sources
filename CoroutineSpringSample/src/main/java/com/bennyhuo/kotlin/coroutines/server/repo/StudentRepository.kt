package com.bennyhuo.kotlin.coroutines.server.repo

import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

@Table("student")
data class Student(
    @Column("name") val name: String,
    @Column("score") val score: Int,
    @Id val id: Long? = null
)

interface StudentRepository : ReactiveCrudRepository<Student, Long> {

    @Query("select * from student where name = :name")
    fun findByName(name: String): Mono<Student>

}