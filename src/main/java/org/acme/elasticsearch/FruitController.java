package org.acme.elasticsearch;

import org.jboss.resteasy.reactive.RestQuery;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Path("/fruits")
public class FruitController {
    @Inject
    FruitService fruitService;

    @POST
    public Response index(Fruit fruit) throws IOException {
        if (fruit.id == null) {
            fruit.id = UUID.randomUUID().toString();
        }
        fruitService.index(fruit);
        return Response.created(URI.create("/fruits/" + fruit.id)).build();
    }

    @GET
    @Path("/{id}")
    public Fruit get(String id) throws IOException {
        return fruitService.get(id);
    }

    @GET
    @Path("/search")
    public List<Fruit> search(@RestQuery String name, @RestQuery String color) throws IOException {
        if (name != null) {
            return fruitService.searchByName(name);
        } else if (color != null) {
            return fruitService.searchByColor(color);
        } else {
            throw new BadRequestException("Should provide name or color query parameter");
        }
    }
}
