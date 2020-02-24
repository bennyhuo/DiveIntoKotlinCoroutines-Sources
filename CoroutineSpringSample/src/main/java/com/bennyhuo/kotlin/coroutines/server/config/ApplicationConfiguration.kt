package com.bennyhuo.kotlin.coroutines.server.config

import com.bennyhuo.kotlin.coroutines.server.repo.Student
import com.bennyhuo.kotlin.coroutines.server.repo.StudentRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.reactive.asPublisher
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component

@Component
class ApplicationConfiguration {


    @Bean
    fun runner(studentRepository: StudentRepository, db: DatabaseClient) = ApplicationRunner {
        val initDb = db.execute {
            """
                CREATE TABLE student (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    score Integer NOT NULL
                );
            """
        }

        val savedStudents = flowOf(
            Student("Harry", 88),
            Student("Hermione", 95),
            Student("Ron", 75),
            Student("Neville", 70)
        ).let {
            studentRepository.saveAll(it.asPublisher())
        }

        initDb.then()
            .thenMany(savedStudents)
            .subscribe()
    }
}