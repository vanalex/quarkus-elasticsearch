package org.acme.elasticsearch

import io.vertx.core.json.JsonObject
import org.apache.http.util.EntityUtils
import org.elasticsearch.client.Request
import org.elasticsearch.client.RestClient
import java.io.IOException
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class FruitService(private val restClient: RestClient) {
    @kotlin.Throws(IOException::class)
    fun index(fruit: Fruit) {
        val request = Request(
            "PUT",
            "/fruits/_doc/" + fruit.id
        )
        request.setJsonEntity(JsonObject.mapFrom(fruit).toString())
        restClient.performRequest(request)
    }

    @kotlin.Throws(IOException::class)
    operator fun get(id: String): Fruit {
        val request = Request(
            "GET",
            "/fruits/_doc/$id"
        )
        val response = restClient.performRequest(request)
        val responseBody = EntityUtils.toString(response.entity)
        val json = JsonObject(responseBody)
        return json.getJsonObject("_source").mapTo(Fruit::class.java)
    }

    @kotlin.Throws(IOException::class)
    fun searchByColor(color: String): List<Fruit> {
        return search("color", color)
    }

    @kotlin.Throws(IOException::class)
    fun searchByName(name: String): List<Fruit> {
        return search("name", name)
    }

    @kotlin.Throws(IOException::class)
    private fun search(term: String, match: String): List<Fruit> {
        val request = Request(
            "GET",
            "/fruits/_search"
        )
        //construct a JSON query like {"query": {"match": {"<term>": "<match"}}
        val termJson = JsonObject().put(term, match)
        val matchJson = JsonObject().put("match", termJson)
        val queryJson = JsonObject().put("query", matchJson)
        request.setJsonEntity(queryJson.encode())
        val response = restClient.performRequest(request)
        val responseBody = EntityUtils.toString(response.entity)
        val json = JsonObject(responseBody)
        val hits = json.getJsonObject("hits").getJsonArray("hits")
        val results: MutableList<Fruit> = ArrayList(hits.size())
        for (i in 0 until hits.size()) {
            val hit = hits.getJsonObject(i)
            val fruit = hit.getJsonObject("_source").mapTo<Fruit>(
                Fruit::class.java
            )
            results.add(fruit)
        }
        return results
    }
}
