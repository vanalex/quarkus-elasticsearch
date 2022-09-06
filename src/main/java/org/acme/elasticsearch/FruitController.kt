package org.acme.elasticsearch

import org.jboss.resteasy.reactive.RestQuery
import java.io.IOException
import java.net.URI
import java.util.*
import javax.ws.rs.BadRequestException
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/fruits")
class FruitController(private val fruitService: FruitService) {
    @POST
    @Throws(IOException::class)
    fun index(fruit: Fruit): Response {
        if (fruit.id == null) {
            fruit.id = UUID.randomUUID().toString()
        }
        fruitService.index(fruit)
        return Response.created(URI.create("/fruits/" + fruit.id)).build()
    }

    @GET
    @Path("/{id}")
    @Throws(IOException::class)
    operator fun get(id: String): Fruit {
        return fruitService[id]
    }

    @GET
    @Path("/search")
    @Throws(IOException::class)
    fun search(@RestQuery name: String?, @RestQuery color: String?): List<Fruit> {
        return if (name != null) {
            fruitService.searchByName(name)
        } else if (color != null) {
            fruitService.searchByColor(color)
        } else {
            throw BadRequestException("Should provide name or color query parameter")
        }
    }
}