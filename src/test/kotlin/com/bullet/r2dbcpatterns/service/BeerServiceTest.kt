package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.AbstractIntegrationTest
import com.bullet.r2dbcpatterns.domain.BeerStyle.STOUT
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.BeerUpdate
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import reactor.test.StepVerifier

@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class BeerServiceTest(@Autowired val beerService: BeerService): AbstractIntegrationTest() {
    @Test
    @Order(1)
    fun shouldSaveBeer() {
        val beer = beerService.save(BeerInsert("Juice Junky", "IPA"))

        StepVerifier
            .create(beer)
            .expectNextMatches { it.name == "Juice Junky" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(2)
    fun shouldFetchBeerById() {
        val beer = beerService.get(1L)

        StepVerifier
            .create(beer)
            .expectNextMatches { it.name == "Juice Junky" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(3)
    fun shouldUpdateBeer() {
        val beer = beerService.update(BeerUpdate(1L, "Oatmeal Stout", "STOUT"))

        StepVerifier
            .create(beer)
            .expectNextMatches { it.name == "Oatmeal Stout" && it.style == STOUT }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(4)
    fun shouldDeleteBeer() {
        val beer = beerService.delete(1L).block()

        StepVerifier
            .create(beerService.get(1L))
            .expectComplete()
            .verify()
    }
}