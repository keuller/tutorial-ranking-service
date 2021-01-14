package com.kmdev.ranking.business

import com.kmdev.ranking.common.Fail
import com.kmdev.ranking.common.Result
import com.kmdev.ranking.common.Success
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CompanyService(private val repository: CompanyRepository) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val DATE_FORMATTER = "yyyy-MM-dd"

    private fun calculateScore(date: LocalDate): Char {
        val years = LocalDate.now().year - date.year

        if (years < 2) return '0'
        if (years in 2..3) return 'C'
        if (years in 4..5) return 'B'
        return 'A'
    }

    fun register(data: RegisterRequest): Result<String> {
        val foundationCompanyDate = LocalDate.parse(data.foundation, DateTimeFormatter.ofPattern(DATE_FORMATTER))
        val companyScore = calculateScore(foundationCompanyDate)
        if (companyScore == '0') return Fail("This company is too much younger.")

        val currentCompany = repository.fetchBySite(data.site)
        if (currentCompany != null) return Fail("There is a company with same site.")

        val entity = Company(
            id = UUID.randomUUID().toString(),
            name = data.name,
            site = data.site,
            foundation = foundationCompanyDate,
            score = companyScore
        )

        return when(val result = repository.save(entity)) {
            is Success -> Success("Company has been created successfully.")
            is Fail -> Fail(cause = result.cause)
        }
    }

    fun ranking(): List<RankingResponse> = repository.fetchRankingList().map { company ->
        RankingResponse(company.id, company.name, company.site, company.score, company.likes, company.dislikes)
    }

    fun vote(data: VoteRequest) {
        when(data.type) {
            "like" -> repository.like(data.id)
            "dislike" -> repository.dislike(data.id)
            else -> logger.warn("Invalid vote type ${data.type}")
        }
    }

}
