package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.messages.CityInsert
import com.bullet.r2dbcpatterns.messages.CityUpdate
import com.bullet.r2dbcpatterns.domain.City
import com.bullet.r2dbcpatterns.repository.CityRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class CityService(private val cityRepository: CityRepository) {
    fun save(insert: CityInsert): Mono<City> = cityRepository.saveCity(insert)

    fun update(update: CityUpdate): Mono<City> = cityRepository.updateCity(update)

    fun get(id: Long): Mono<City> = cityRepository.get(id)

    fun getAll(): Flux<City> = cityRepository.getAll()

    fun delete(id: Long): Mono<Void> = cityRepository.deleteById(id)
}