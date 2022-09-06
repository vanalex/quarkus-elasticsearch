package org.acme.elasticsearch;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

@QuarkusTest
public class FruitServiceTest {

    @Inject
    FruitService fruitService;

    @Test
    void testSearch() throws IOException {
        Fruit fruit = Fruit.builder().color("red").name("strawberry").build();
        fruitService.index(fruit);
        List<Fruit> searchResult = fruitService.searchByColor("red");
        Assertions.assertFalse(searchResult.isEmpty());
        Fruit fruitFounded = searchResult.stream().findFirst().orElse(Fruit.builder().build());
        Assertions.assertEquals(fruitFounded.color, "red");
        Assertions.assertEquals(fruitFounded.name, "strawberry");
    }
}
