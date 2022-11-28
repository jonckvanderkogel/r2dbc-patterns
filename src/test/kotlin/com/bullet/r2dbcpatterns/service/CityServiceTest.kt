package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.AbstractIntegrationTest
import com.bullet.r2dbcpatterns.messages.CityInsert
import com.bullet.r2dbcpatterns.messages.CityUpdate
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import reactor.test.StepVerifier

@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@SpringBootTest
class CityServiceTest(@Autowired val cityService: CityService): AbstractIntegrationTest() {

    @Test
    @Order(1)
    fun shouldSaveCity() {
        val city = cityService.save(CityInsert("Utrecht"))

        StepVerifier
            .create(city)
            .expectNextMatches{ it.name == "Utrecht" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(2)
    fun shouldFetchCityById() {
        val city = cityService.get(1L)

        StepVerifier
            .create(city)
            .expectNextMatches{ it.name == "Utrecht" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(3)
    fun shouldUpdateCity() {
        val cityUpdate = cityService.update(CityUpdate(1L, "Amsterdam"))

        StepVerifier
            .create(cityUpdate)
            .expectNextMatches{ it.name == "Amsterdam" }
            .expectComplete()
            .verify()
    }

    @Test
    @Order(4)
    fun shouldDeleteCity() {
        cityService.delete(1L).block()

        StepVerifier
            .create(cityService.get(1L))
            .expectComplete()
            .verify()
    }
}