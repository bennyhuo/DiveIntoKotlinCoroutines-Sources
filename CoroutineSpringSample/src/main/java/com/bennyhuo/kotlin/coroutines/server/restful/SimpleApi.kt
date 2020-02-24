package com.bennyhuo.kotlin.coroutines.server.restful

import com.bennyhuo.kotlin.coroutines.server.repo.StudentRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/rest/students")
class SimpleApi(val repository: StudentRepository) {

    @GetMapping("/")
    suspend fun listStudent() = repository.findAll().asFlow().toList()

    @GetMapping("/id/{id}")
    suspend fun getById(@PathVariable("id") id: Long) = repository.findById(id).awaitSingle()

    @GetMapping("/name/{name}")
    suspend fun getByName(@PathVariable("name") name: String) = repository.findByName(name).awaitFirst()

}