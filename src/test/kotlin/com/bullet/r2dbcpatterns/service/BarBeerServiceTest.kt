package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.AbstractIntegrationTest
import com.bullet.r2dbcpatterns.messages.BarBeerInsert
import com.bullet.r2dbcpatterns.messages.BarInsert
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.CityInsert
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
class BarBeerServiceTest(
    @Autowired val cityService: CityService,
    @Autowired val barService: BarService,
    @Autowired val beerService: BeerService,
    @Autowired val barBeerService: BarBeerService
): AbstractIntegrationTest() {
    @Test
    @Order(1)
    fun shouldLinkBeerToBar() {
        val barBeer = cityService
            .save(CityInsert("Arnhem"))
            .flatMap { barService
                .save(BarInsert("Vrijdag", it.id))
                .zipWith( beerService.save(BeerInsert("Bird of Prey", "IPA")) )
            }
            .flatMap { (bar, beer) -> barBeerService.save(BarBeerInsert(bar.id, beer.id)) }

        StepVerifier
            .create(barBeer)
            .expectNextMatches { it.barId == 1L && it.beerId == 1L }
            .expectComplete()
            .verify()

        StepVerifier
            .create(barService.get(1L))
            .expectNextMatches { it.beers.size == 1 }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(2)
    fun shouldDeleteLink() {
        barBeerService.delete(1L).block()

        StepVerifier
            .create(barService.get(1L))
            .expectNextMatches { it.beers.isEmpty() }
            .expectComplete()
            .verify()

        StepVerifier
            .create(beerService.get(1L))
            .expectNextMatches { it.name == "Bird of Prey" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(3)
    fun whenLinkedBeerIsDeletedAssociatedBarBeerShouldCascade() {
        val barBeer = barBeerService.save(BarBeerInsert(1L, 1L)).block()

        beerService.delete(1L).block()

        StepVerifier
            .create(barBeerService.get(barBeer!!.id))
            .expectComplete()
            .verify()
    }

    @Test
    @Order(4)
    fun whenLinkedBarIsDeletedAssociatedBarBeerShouldCascade() {
        val barBeer = beerService
            .save(BeerInsert("Bird of Prey", "IPA"))
            .flatMap { barBeerService.save(BarBeerInsert(1L, it.id)) }
            .block()

        barService.delete(1L).block()

        StepVerifier
            .create(barBeerService.get(barBeer!!.id))
            .expectComplete()
            .verify()
    }
}