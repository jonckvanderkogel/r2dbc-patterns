package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.BeerUpdate
import com.bullet.r2dbcpatterns.repository.BeerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BeerService(private val beerRepository: BeerRepository) {
    fun save(insert: BeerInsert): Mono<Beer> = beerRepository.saveBeer(insert)

    fun update(input: BeerUpdate): Mono<Beer> = beerRepository.updateBeer(input)

    fun get(id: Long): Mono<Beer> = beerRepository.findById(id)

    fun getAll(): Flux<Beer> = beerRepository.findAll()

    fun delete(id: Long): Mono<Void> = beerRepository.deleteById(id)
}