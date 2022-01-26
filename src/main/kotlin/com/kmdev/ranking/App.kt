package com.kmdev.ranking

import com.kmdev.ranking.application.http.Router
import com.kmdev.ranking.application.infra.Database
import org.http4k.cloudnative.health.Health
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("com.kmdev.ranking.Application")

    // health check endpoints (liveness & readiness)
    val app = Health(Router().routes())

    // check database connection
    Database.check()

    // creates app server based on routes
    val server = app.asServer(Netty(8080))
    server.start()

    // register runtime hook to stop server
    Runtime.getRuntime().addShutdownHook(Thread { server.stop() })

    logger.info("Server has been started at http://0.0.0.0:${server.port()}")
}
