package com.example.repository

import com.example.domain.TweetQuery
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.time.ZoneOffset
import java.util.*

// TODO: Integration test with in-memory database?
class QueryRepository {

    fun insertQuery(query: TweetQuery) =
        transaction {
            QueryTable.insert {
                it[id] = query.id
                it[name] = query.nombre
                // TODO: I switch to Joda Datetime which was easier to integrate with everything(JSON SerDes and Exposed), should we keep it?
//                it[startDate] = query.dateRange?.start?.toInstant(ZoneOffset.of("America/Guayaquil"))?.let { DateTime(it) }
                it[startDate] = query.dateRange?.start?.toDateTime()
//                it[endDate] = query.dateRange?.end?.toInstant(ZoneOffset.of("America/Guayaquil"))?.let { DateTime(it) }
                it[endDate] = query.dateRange?.end?.toDateTime()
                it[allowRetweets] = query.allowRetweets
            }

            insertHashTags(query.hashTags, query.id)
        }

    private fun insertHashTags(hashTags: List<String>, queryId: UUID) =
        HashtagTable.batchInsert(hashTags) {
            this[HashtagTable.idQuery] = queryId
            this[HashtagTable.text] = it
        }.map { it[HashtagTable.id] }
}

// TODO: Explain kotlin object
// TODO: POSTGRES end up beign case-sensitive :( Should I use underscore instead of Uppercase. Ask Pablo his standard for naming columns
object QueryTable: Table("Query") {
    val id = uuid("IdQuery").primaryKey()
    val name = varchar("NameQuery", 50).uniqueIndex()
    val startDate = datetime("StartDateQuery").nullable()
    val endDate = datetime("EndDateQuery").nullable()
    val allowRetweets = bool("AllowRetweetsQuery")
}

object HashtagTable: Table("HashTag") {
    val id = integer("IdHashTag").autoIncrement().primaryKey()
    val idQuery = uuid("IdQuery").references(QueryTable.id)
    val text = varchar("TextHashTag", 50)
}
