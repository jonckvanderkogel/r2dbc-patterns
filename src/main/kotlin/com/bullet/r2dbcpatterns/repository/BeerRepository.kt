package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.domain.BeerStyle
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface BeerRepository: ReactiveCrudRepository<Beer, Long>, BeerRepositoryEntityTemplate {
    fun findAllByStyle(style: BeerStyle): Flux<Beer>

    @Query("SELECT COUNT(*) FROM beer LEFT JOIN bar_beer ON beer.id = bar_beer.beer_id WHERE bar_beer.id IS NULL")
    fun getCountOfBeersNotAssociatedWithBar(): Mono<Int>
}