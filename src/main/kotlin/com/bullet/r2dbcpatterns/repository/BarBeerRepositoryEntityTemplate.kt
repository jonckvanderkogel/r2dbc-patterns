package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.BarBeer
import com.bullet.r2dbcpatterns.messages.BarBeerInsert
import reactor.core.publisher.Mono

interface BarBeerRepositoryEntityTemplate {
    fun saveBarBeer(insert: BarBeerInsert): Mono<BarBeer>
}