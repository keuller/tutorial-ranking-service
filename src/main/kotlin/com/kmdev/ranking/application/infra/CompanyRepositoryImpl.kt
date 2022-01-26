package com.kmdev.ranking.application.infra

import com.kmdev.ranking.business.Company
import com.kmdev.ranking.business.CompanyRepository
import com.kmdev.ranking.common.Fail
import com.kmdev.ranking.common.Result
import com.kmdev.ranking.common.Success
import org.jdbi.v3.core.kotlin.mapTo

class CompanyRepositoryImpl : CompanyRepository {

    override fun save(value: Company): Result<Unit> = try {
        Database.runCommand {
            it.createUpdate(Database.getSql("db.insertCompany"))
                .bindBean(value)
                .execute()
        }
        Success(Unit)
    } catch(ex: Exception) {
        Fail("Cannot insert company", ex)
    }

    override fun fetchBySite(value: String): Company? {
        val entity = Database.runCommand {
            it.createQuery(Database.getSql("db.fetchBySite"))
                .bind("site", value)
                .mapTo<Company>()
                .findOne()
        }

        return if (entity.isPresent) entity.get() else null
    }

    override fun fetchRankingList(): List<Company> = Database.runCommand {
        it.createQuery(Database.getSql("db.fetchCompanies"))
            .mapTo(Company::class.java)
            .list()
    }

    override fun like(id: String) {
        Database.runCommandInTransaction {
            it.createUpdate("UPDATE companies SET likes = likes + 1 WHERE id = :id")
                .bind("id", id).execute()
        }
    }

    override fun dislike(id: String) {
        Database.runCommandInTransaction {
            it.createUpdate("UPDATE companies SET dislikes = dislikes + 1 WHERE id = :id")
                .bind("id", id).execute()
        }
    }

}
