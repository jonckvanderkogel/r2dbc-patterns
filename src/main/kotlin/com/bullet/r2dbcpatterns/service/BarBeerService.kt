package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.messages.BarBeerInsert
import com.bullet.r2dbcpatterns.domain.BarBeer
import com.bullet.r2dbcpatterns.repository.BarBeerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BarBeerService(val barBeerRepository: BarBeerRepository) {
    fun save(insert: BarBeerInsert): Mono<BarBeer> = barBeerRepository.save(BarBeer.of(insert))

    fun delete(id: Long): Mono<Void> = barBeerRepository.deleteById(id)
}