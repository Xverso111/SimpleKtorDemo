package com.example.repository

import com.example.domain.SearchCriteria
import com.example.moshi
import com.example.repository.SearchCriteriaTable.allowRetweets
import com.example.repository.SearchCriteriaTable.endDate
import com.example.repository.SearchCriteriaTable.hashTags
import com.example.repository.SearchCriteriaTable.id
import com.example.repository.SearchCriteriaTable.name
import com.example.repository.SearchCriteriaTable.startDate
import com.example.service.DateRange
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

// TODO: Integration test with in-memory database?
class SearchCriteriaRepository {

    fun insertQuery(query: SearchCriteria) =
        transaction {
            SearchCriteriaTable.insert {
                it[id] = UUID.randomUUID()
                it[name] = query.name
                it[hashTags] = query.hashTags
                it[startDate] = query.dateRange?.start?.toDateTime()
                it[endDate] = query.dateRange?.end?.toDateTime()
                it[allowRetweets] = query.allowRetweets
            }
        } get id

    fun getQuery(id: UUID) =
        transaction {
            SearchCriteriaTable
                .select { SearchCriteriaTable.id eq id }
                .map { it.toSearchCriteria() }
                .firstOrNull()
        }
}

fun ResultRow.toSearchCriteria() = SearchCriteria(
    this[name],
    this[hashTags],
    this.toDateRange(),
    this[allowRetweets]
)

fun ResultRow.toDateRange(): DateRange? {
    val startDate = this[startDate]?.toLocalDateTime()
    val endDate = this[endDate]?.toLocalDateTime()
    return if (startDate != null && endDate != null) DateRange(startDate, endDate) else null
}

// TODO: Explain kotlin object
// TODO: POSTGRES end up being case-sensitive :( Should I use underscore instead of Uppercase. Ask Pablo his standard for naming columns
object SearchCriteriaTable: Table("search_criteria") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 50).uniqueIndex()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    // TODO: save the hashtags on a JSONB array
    val hashTags = jsonb("hash_tags", moshi)
    val allowRetweets = bool("allow_retweets")
}
