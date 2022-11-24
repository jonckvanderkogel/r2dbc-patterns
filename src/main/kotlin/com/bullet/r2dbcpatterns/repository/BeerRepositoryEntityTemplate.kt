package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.BeerUpdate
import reactor.core.publisher.Mono

interface BeerRepositoryEntityTemplate {
    fun saveBeer(insert: BeerInsert): Mono<Beer>

    fun updateBeer(update: BeerUpdate): Mono<Beer>
}