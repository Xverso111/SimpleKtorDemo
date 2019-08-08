package com.example.repository

import com.squareup.moshi.Moshi
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import java.sql.PreparedStatement
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types.newParameterizedType
import java.lang.reflect.ParameterizedType

fun Table.jsonb(name: String, moshi: Moshi): Column<List<String>> = registerColumn(name, Json(moshi))

private class Json(moshi: Moshi): ColumnType() {

    val type: ParameterizedType = newParameterizedType(List::class.java, String::class.java)
    val adapter: JsonAdapter<List<String>> = moshi.adapter(type)

    override fun sqlType() = "jsonb"

    override fun setParameter(stmt: PreparedStatement, index: Int, value: Any?) {
        val obj = PGobject()
        obj.type = "jsonb"
        obj.value = value as String
        stmt.setObject(index, obj)
    }

    override fun valueFromDB(value: Any): Any {
        value as PGobject
        return try {
            adapter.fromJson(value.value) as Any
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Can't parse JSON: $value")
        }
    }

    override fun notNullValueToDB(value: Any): Any = adapter.toJson(value as List<String>)
    override fun nonNullValueToString(value: Any): String = adapter.toJson(value as List<String>)
}
