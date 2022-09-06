package org.acme.elasticsearch

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.IOException
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest
internal class FruitServiceTest(){

    @Inject
    @field: Default
    lateinit var fruitService: FruitService

    @Test
    @Throws(IOException::class)
    fun testSearch() {
        val id = UUID.randomUUID().toString()
        val fruit = Fruit(id, "strawberry", "red")
        fruitService.index(fruit)
        val searchResult = fruitService.searchByColor("red")
        assertFalse(searchResult.isEmpty())
        val fruitFounded = searchResult.stream().findFirst().orElse(Fruit())
        assertEquals(fruitFounded.color, "red")
        assertEquals(fruitFounded.name, "strawberry")
    }
}