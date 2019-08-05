package com.example.repository

import com.example.domain.TweetQuery
import com.example.service.DateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

// TODO: Integration test with in-memory database?
class QueryRepository {

    suspend fun insertQuery(query: TweetQuery) =
        // TODO: Do we really want to do this async? Is it overengineering?
        withContext(Dispatchers.IO) {
            transaction {
                QueryTable.insert {
                    it[id] = query.id
                    it[name] = query.nombre
                    // TODO: I switch to Joda Datetime which was easier to integrate with everything(JSON SerDes and Exposed), should we keep it?
                    it[startDate] = query.dateRange?.start?.toDateTime()
                    it[endDate] = query.dateRange?.end?.toDateTime()
                    it[allowRetweets] = query.allowRetweets
                }

                insertHashTags(query.hashTags, query.id)
            }
        }

    private fun insertHashTags(hashTags: List<String>, queryId: UUID) =
        HashtagTable.batchInsert(hashTags) {
            this[HashtagTable.idQuery] = queryId
            this[HashtagTable.text] = it
        }.map { it[HashtagTable.id] }

    suspend fun getQuery(id: UUID) =
        withContext(Dispatchers.IO) {
            transaction {
                QueryTable
                    .innerJoin(HashtagTable)
                    .select { QueryTable.id eq id }
                    .asSequence()
                    .groupBy { it[QueryTable.id] }
                    .map {
                        it.value.first().let { row ->
                            TweetQuery(
                                row[QueryTable.id],
                                row[QueryTable.name],
                                it.value.map { hashtag ->
                                    hashtag[HashtagTable.text]
                                },
                                row[QueryTable.startDate]?.let {
                                    DateRange(
                                        // TODO: Explain that double bangs should be avoided at all costs!!!
                                        row[QueryTable.startDate]!!.toLocalDateTime(),
                                        row[QueryTable.endDate]!!.toLocalDateTime()
                                    )
                                },
                                row[QueryTable.allowRetweets]
                            )
                        }

                    }.firstOrNull()
        }
    }
}

// TODO: Explain kotlin object
// TODO: POSTGRES end up being case-sensitive :( Should I use underscore instead of Uppercase. Ask Pablo his standard for naming columns
object QueryTable: Table("query") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 50).uniqueIndex()
    val startDate = datetime("start_date").nullable()
    val endDate = datetime("end_date").nullable()
    // TODO: save the hashtags on a JSONB array
    val allowRetweets = bool("allow_retweets")
}

object HashtagTable: Table("hash_tag") {
    val id = integer("id").autoIncrement().primaryKey()
    val idQuery = uuid("id_query").references(QueryTable.id)
    val text = varchar("text", 50)
}
