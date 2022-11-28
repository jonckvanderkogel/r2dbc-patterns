package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.AbstractIntegrationTest
import com.bullet.r2dbcpatterns.domain.BeerStyle.*
import com.bullet.r2dbcpatterns.messages.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.util.function.Tuple2

@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class BarServiceTest(
    @Autowired val barService: BarService,
    @Autowired val beerService: BeerService,
    @Autowired val barBeerService: BarBeerService,
    @Autowired val cityService: CityService
) : AbstractIntegrationTest() {
    @Test
    @Order(1)
    fun shouldSaveBar() {
        val bar = cityService
            .save(CityInsert("Arnhem"))
            .flatMap { barService.save(BarInsert("Vrijdag", it.id)) }

        StepVerifier
            .create(bar)
            .expectNextMatches { it.name == "Vrijdag" && it.beers.isEmpty() }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(2)
    fun shouldLinkBeers() {
        Flux.merge(
            beerService.save(BeerInsert(name = "Punk IPA", style = "IPA")),
            beerService.save(BeerInsert(name = "Imperial Stout", style = "STOUT"))
        )
            .flatMap { barBeerService.save(BarBeerInsert(1L, it.id)) }
            .blockLast()

        StepVerifier
            .create(barService.get(1L))
            .expectNextMatches { it.beers.size == 2 }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(3)
    fun shouldUpdateBar() {
        val updatedBar = cityService.save(CityInsert("Utrecht"))
            .flatMap { barService.update(BarUpdate(1L, "Frontaal Bar", it.id)) }
            .block()

        StepVerifier
            .create(barService.get(1L).zipWith(cityService.get(2L)))
            .expectNextMatches { (bar, city) -> bar.name == "Frontaal Bar" && city.bars.contains(bar) }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(4)
    fun shouldDeleteBar() {
        barService.delete(1L).block()

        StepVerifier
            .create(barService.get(1L))
            .expectComplete()
            .verify()

        // Beers should nog get deleted when the bar get's deleted
        StepVerifier
            .create(beerService.getAll())
            .expectNextCount(2L)
            .expectComplete()
            .verify()
    }


}