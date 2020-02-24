package com.bennyhuo.kotlin.coroutine.vertx

import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.core.json.array
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.ext.sql.executeAwait
import io.vertx.kotlin.ext.sql.getConnectionAwait
import io.vertx.kotlin.ext.sql.queryWithParamsAwait
import kotlinx.coroutines.launch


class MainCoroutineVerticle : CoroutineVerticle() {

    private lateinit var client: JDBCClient

    override suspend fun start() {

        client = JDBCClient.createShared(vertx, json {
            obj(
                    "url" to "jdbc:hsqldb:mem:test?shutdown=true",
                    "driver_class" to "org.hsqldb.jdbcDriver",
                    "max_pool_size-loop" to 30
            )
        })

        // Populate database
        val statements = listOf(
                "CREATE TABLE student (id INTEGER IDENTITY PRIMARY KEY, name VARCHAR(255) NOT NULL, score INTEGER NOT NULL)",
                "INSERT INTO student (name, score) VALUES 'Harry', 88",
                "INSERT INTO student (name, score) VALUES 'Hermione', 95",
                "INSERT INTO student (name, score) VALUES 'Ron', 75",
                "INSERT INTO student (name, score) VALUES 'Neville', 70"
        )
        client.getConnectionAwait()
                .use { connection -> statements.forEach { connection.executeAwait(it) } }

        // Build Vert.x Web router
        val router = Router.router(vertx)
        router.get("/id/:id").coroutineHandler { routingContext ->
            val id = routingContext.pathParam("id")
            val result = client.queryWithParamsAwait("SELECT * FROM student WHERE id=?", json { array(id) })

            if (result.rows.size == 1) {
                routingContext.response().end(json {
                    result.rows[0].encode()
                })
            } else {
                routingContext.response().setStatusCode(404).end()
            }
        }

        // Start the server
        vertx.createHttpServer()
                .requestHandler(router)
                .listenAwait(config.getInteger("http.port", 8081))
    }

    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch(coroutineContext) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }
}
