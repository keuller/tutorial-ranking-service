package com.kmdev.ranking.infra

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.locator.ClasspathSqlLocator
import org.jdbi.v3.core.statement.UnableToCreateStatementException
import org.jdbi.v3.core.statement.UnableToExecuteStatementException
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import java.nio.file.Paths
import java.util.*

// generic JDBI handler to perform SQL commands
typealias CommandHandler<T> = (Handle) -> T

object Database {
    private var jdbi: Jdbi? = null
    private val path = Paths.get("").toAbsolutePath().toString()

    private fun getSqlLocator() = ClasspathSqlLocator.create()

    fun init() {
        runCommand {
            it.createScript(getSql("db.create_table")).execute()
        }
    }

    private fun getHanaDataSource(): HikariDataSource = HikariConfig().let {
        val codes = UUID.randomUUID().toString().split("-")
        it.jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        it.poolName = "company_${codes[0]}_pool"
        it.connectionTestQuery = "SELECT 1"
        it.maximumPoolSize = 10
        it.connectionTimeout = 5_000L
        it.leakDetectionThreshold = 15_000L

        HikariDataSource(it)
    }

    private fun getJdbi(): Jdbi {
        if (null != jdbi) return jdbi!!
        return getHanaDataSource().let { dataSource ->
            if (null == jdbi) {
                jdbi = Jdbi.create(dataSource)
                    .installPlugin(KotlinPlugin())
                    .installPlugin(KotlinSqlObjectPlugin())
            }
            jdbi!!
        }
    }

    fun getSql(location: String): String = getSqlLocator().locate(location)

    fun <T> runCommand(fn: CommandHandler<T>): T = try {
        getJdbi().withHandle<T, Exception>(fn)
    } catch(ex: UnableToExecuteStatementException) {
        throw Exception(ex.cause)
    } catch (ex: UnableToCreateStatementException) {
        throw Exception("Could not create statement - ${ex.message}")
    }

    fun <T> runCommandInTransaction(fn: CommandHandler<T>): T = try {
        getJdbi().inTransaction<T, Exception>(fn)
    } catch(ex: UnableToExecuteStatementException) {
        throw Exception(ex.cause)
    } catch (ex: UnableToCreateStatementException) {
        throw Exception("Could not create statement - ${ex.message}")
    }
}
