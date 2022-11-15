package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.messages.CityInsert
import com.bullet.r2dbcpatterns.messages.CityUpdate
import com.bullet.r2dbcpatterns.domain.City
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CityRepositoryEntityTemplate {
    fun saveCity(insert: CityInsert): Mono<City>

    fun updateCity(update: CityUpdate): Mono<City>

    fun get(id: Long): Mono<City>

    fun getAll(): Flux<City>
}