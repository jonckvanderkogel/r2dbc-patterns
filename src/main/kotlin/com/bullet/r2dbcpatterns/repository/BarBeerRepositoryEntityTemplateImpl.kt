package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.BarBeer
import com.bullet.r2dbcpatterns.messages.BarBeerInsert
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

class BarBeerRepositoryEntityTemplateImpl(private val template: R2dbcEntityTemplate): BarBeerRepositoryEntityTemplate {
    override fun saveBarBeer(insert: BarBeerInsert): Mono<BarBeer> {
        return template.databaseClient
            .sql("INSERT INTO bar_beer (bar_id, beer_id) VALUES ($1, $2) RETURNING id, bar_id AS \"barId\", beer_id AS \"beerId\"")
            .bind("$1", insert.barId)
            .bind("$2", insert.beerId)
            .fetch()
            .first()
            .flatMap {
                ReflectionUtil.deserializeToPojo(it, BarBeer::class.java)
            }
    }
}