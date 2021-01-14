package com.kmdev.ranking.application.http

import com.kmdev.ranking.application.infra.CompanyRepositoryImpl
import com.kmdev.ranking.business.CompanyService
import com.kmdev.ranking.business.RankingResponse
import com.kmdev.ranking.business.RegisterRequest
import com.kmdev.ranking.business.VoteRequest
import com.kmdev.ranking.common.Fail
import com.kmdev.ranking.common.Success
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.core.Method.PATCH
import org.http4k.core.Method.POST
import org.http4k.format.KotlinxSerialization.auto
import org.http4k.routing.*

class Router {
    private val service = CompanyService(CompanyRepositoryImpl())

    private fun index(): HttpHandler = {
        Response(Status.OK).body("It works!")
    }

    // creates application routes
    fun routes(): RoutingHttpHandler = routes(
        "/" bind GET to index(),
        "/v1/companies" bind routes(
            "/" bind POST to registerHandler(service),
            "/" bind GET to rankingHandler(service),
            "/vote" bind PATCH to voteHandler(service)
        )
    )

    fun registerHandler(service: CompanyService): HttpHandler {
        val registerData = Body.auto<RegisterRequest>().toLens()
        return { request ->
            val data = registerData(request)

            when(val result = service.register(data)) {
                is Success -> Response(Status.CREATED).body(result.value)
                is Fail -> Response(Status.BAD_REQUEST).body(result.cause)
            }
        }
    }

    fun rankingHandler(service: CompanyService): HttpHandler = {
        val rankingResponse = Body.auto<List<RankingResponse>>().toLens()
        rankingResponse(service.ranking(), Response(Status.OK))
    }

    fun voteHandler(service: CompanyService): HttpHandler = { request ->
        val voteRequestBody = Body.auto<VoteRequest>().toLens()
        val voteRequest = voteRequestBody(request)
        service.vote(voteRequest)
        Response(Status.NO_CONTENT)
    }
}
