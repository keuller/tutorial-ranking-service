package com.kmdev.ranking.application.http

import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.PUT
import org.http4k.core.Method.POST
import org.http4k.core.Method.DELETE
import org.http4k.routing.*

class Router {

    // template handler
    private fun index(): HttpHandler = { 
        Response(Status.OK).body("It works!")
    }

    // creates application routes
    fun routes(): RoutingHttpHandler = routes(
        "/" bind GET to index(),
    )

}
