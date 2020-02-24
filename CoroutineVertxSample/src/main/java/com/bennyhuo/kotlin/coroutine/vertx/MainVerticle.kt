package com.bennyhuo.kotlin.coroutine.vertx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class MainVerticle : AbstractVerticle() {

  override fun start(startFuture: Future<Void>) {
    val router = Router.router(vertx).apply {
      get("/Hello").handler { event ->
        event.response().end("Hello Vert.x!!!")
      }
    }

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080) { result ->
        if (result.succeeded()) {
          startFuture.complete()
        } else {
          startFuture.fail(result.cause())
        }
      }
  }
}
