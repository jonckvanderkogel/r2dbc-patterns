package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.domain.BeerStyle
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.BeerUpdate
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

class BeerRepositoryEntityTemplateImpl(private val template: R2dbcEntityTemplate): BeerRepositoryEntityTemplate {
    override fun saveBeer(insert: BeerInsert): Mono<Beer> {
        return template.databaseClient
            .sql("INSERT INTO beer (name, style) VALUES ($1, $2) RETURNING id, name, style")
            .bind("$1", insert.name)
            .bind("$2", BeerStyle.valueOf(insert.style).name)
            .fetch()
            .first()
            .flatMap {
                ReflectionUtil.deserializeToPojo(it, Beer::class.java)
            }
    }

    override fun updateBeer(update: BeerUpdate): Mono<Beer> {
        return template.databaseClient
            .sql("UPDATE beer SET name = $2, style = $3 WHERE id = $1 RETURNING id, name, style")
            .bind("$1", update.id)
            .bind("$2", update.name)
            .bind("$3", BeerStyle.valueOf(update.style).name)
            .fetch()
            .first()
            .flatMap {
                ReflectionUtil.deserializeToPojo(it, Beer::class.java)
            }
    }
}