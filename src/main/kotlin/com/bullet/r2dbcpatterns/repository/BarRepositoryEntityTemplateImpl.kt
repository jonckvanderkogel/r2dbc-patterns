package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.dbinput.BarInsert
import com.bullet.r2dbcpatterns.dbinput.BarUpdate
import com.bullet.r2dbcpatterns.domain.Bar
import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.domain.BeerStyle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Mono

class BarRepositoryEntityTemplateImpl(val template: R2dbcEntityTemplate): BarRepositoryEntityTemplate {
    override fun saveBar(insert: BarInsert): Mono<Bar> {
        return template.databaseClient
            .sql("""
                WITH ins AS
                ( INSERT INTO
                        bar
                        (
                            name,
                            city_id
                        )
                        VALUES
                        (
                            $1,$2
                        )
                        RETURNING id,
                        name )
                SELECT
                    ins.id AS "bar.id",
                    ins.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    ins
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = ins.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
            """.trimIndent())
            .bind("$1", insert.name)
            .bind("$2", insert.cityId)
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("bar.id") }
            .map { constructBar(it) }
            .next()
    }

    override fun updateBar(update: BarUpdate): Mono<Bar> {
        return template.databaseClient
            .sql("""
                WITH upd AS
                ( UPDATE bar 
                    SET name = $2,city_id = $3
                    WHERE id = $1
                    RETURNING id,name
                )
                SELECT
                    upd.id AS "bar.id",
                    upd.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    upd
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = upd.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
            """.trimIndent())
            .bind("$1", update.id)
            .bind("$2", update.name)
            .bind("$3", update.cityId)
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("bar.id") }
            .map { constructBar(it) }
            .next()
    }

    private fun constructBar(data: List<Map<String, Any>>): Bar {
        val head = data.first()
        return Bar(
            id = head.get("bar.id") as Long,
            name = head.get("bar.name") as String,
            beers = constructBeers(
                data.filter { it.get("bar_beer.id") != null }
            )
        )
    }

    private fun constructBeers(data: List<Map<String, Any>>): List<Beer> = data.map { constructBeer(it) }.toList()

    private fun constructBeer(data: Map<String, Any>): Beer {
        return Beer(
            id = data.get("beer.id") as Long,
            name = data.get("beer.name") as String,
            style = BeerStyle.fromString(data.get("beer.style") as String)
        )
    }
}