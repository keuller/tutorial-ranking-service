package com.kmdev.ranking

import com.kmdev.ranking.application.http.Router
import org.http4k.cloudnative.health.Health
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory

fun main() {
    val HTTP_PORT = System.getenv().getOrDefault("APP_PORT", "8080")
    val logger = LoggerFactory.getLogger("com.kmdev.ranking.Application")

    // health check endpoints (liveness & readiness)
    val app = Health(Router().routes())

    // creates app server based on routes
    val server = app.asServer(Netty(HTTP_PORT.toInt()))
    server.start()

    // register runtime hook to stop server
    Runtime.getRuntime().addShutdownHook(Thread { server.stop() })

    logger.info("Server has been started at http://0.0.0.0:${server.port()}")
}
