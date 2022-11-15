package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.dbinput.BeerInput
import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.repository.BeerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class BeerService(private val beerRepository: BeerRepository) {
    fun save(beerInput: BeerInput): Mono<Beer> = beerRepository.save(Beer.of(beerInput))

    fun update(beerInput: BeerInput): Mono<Beer> = beerRepository.save(Beer.of(beerInput))

    fun get(id: Long): Mono<Beer> = beerRepository.findById(id)

    fun getAll(): Flux<Beer> = beerRepository.findAll()

    fun delete(id: Long): Mono<Void> = beerRepository.deleteById(id)
}