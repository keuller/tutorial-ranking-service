package com.kmdev.ranking.business

import com.kmdev.ranking.common.Result

interface CompanyRepository {

    fun save(value: Company): Result<Unit>

    fun fetchBySite(value: String): Company?

    fun fetchRankingList(): List<Company>

    fun like(id: String)

    fun dislike(id: String)
}
