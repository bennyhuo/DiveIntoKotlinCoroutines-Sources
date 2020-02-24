package com.bennyhuo.kotlin.coroutines.server.route

import com.bennyhuo.kotlin.coroutines.server.repo.StudentRepository
import kotlinx.coroutines.reactive.asFlow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class SimpleRoute(val repository: StudentRepository) {

    @Bean
    fun students() = router {
        "/route/students".nest {
            GET("/") { request ->
                repository.findAll().let {
                    ServerResponse.ok().body(it)
                }
            }

            GET("/id/{id}") { request ->
                repository.findById(request.pathVariable("id").toLong()).let {
                    ServerResponse.ok().body(it)
                }
            }

            GET("/name/{name}") { request ->
                repository.findByName(request.pathVariable("name")).let {
                    ServerResponse.ok().body(it)
                }
            }
        }
    }

    @Bean
    fun studentsCoroutine() = coRouter {
        "/co-route/students".nest {
            GET("/") { request ->
                repository.findAll()
                    .asFlow().let {
                        ServerResponse.ok().bodyAndAwait(it)
                    }
            }

            GET("/id/{id}") { request ->
                repository.findById(request.pathVariable("id").toLong())
                    .asFlow().let {
                        ServerResponse.ok().bodyAndAwait(it)
                    }
            }

            GET("/name/{name}") { request ->
                repository.findByName(request.pathVariable("name"))
                    .asFlow().let {
                        ServerResponse.ok().bodyAndAwait(it)
                    }
            }
        }
    }
}