package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.messages.CityInsert
import com.bullet.r2dbcpatterns.messages.CityUpdate
import com.bullet.r2dbcpatterns.domain.Bar
import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.domain.BeerStyle
import com.bullet.r2dbcpatterns.domain.City
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CityRepositoryEntityTemplateImpl(val template: R2dbcEntityTemplate): CityRepositoryEntityTemplate {
    override fun saveCity(insert: CityInsert): Mono<City> {
        return template.databaseClient
            .sql("""
                WITH ins AS
                ( INSERT INTO
                        city
                        (
                            name
                        )
                        VALUES
                        (
                            $1
                        )
                        RETURNING id,
                        name )
                SELECT
                    ins.id AS "city.id",
                    ins.name AS "city.name",
                    bar.id AS "bar.id",
                    bar.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    ins
                LEFT JOIN
                    bar
                ON
                    bar.city_id = ins.id
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = bar.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
                ORDER BY bar.id, beer.id
            """.trimIndent())
            .bind("$1", insert.name)
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("city.id") }
            .map { constructCity(it) }
            .next()
    }

    override fun updateCity(update: CityUpdate): Mono<City> {
        return template.databaseClient
            .sql("""
                WITH upd AS
                ( UPDATE city 
                    SET name = $2
                    WHERE id = $1
                    RETURNING id,name
                )
                SELECT
                    upd.id AS "city.id",
                    upd.name AS "city.name",
                    bar.id AS "bar.id",
                    bar.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    upd
                LEFT JOIN
                    bar
                ON
                    bar.city_id = upd.id
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = bar.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
                ORDER BY bar.id, beer.id
            """.trimIndent())
            .bind("$1", update.id)
            .bind("$2", update.name)
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("city.id") }
            .map { constructCity(it) }
            .next()
    }

    override fun get(id: Long): Mono<City> {
        return template.databaseClient
            .sql("""
                SELECT
                    city.id AS "city.id",
                    city.name AS "city.name",
                    bar.id AS "bar.id",
                    bar.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    city
                LEFT JOIN
                    bar
                ON
                    bar.city_id = city.id
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = bar.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
                WHERE
                    city.id = $1
                ORDER BY bar.id, beer.id
            """.trimIndent())
            .bind("$1", id)
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("city.id") }
            .map { constructCity(it) }
            .next()
    }

    override fun getAll(): Flux<City> {
        return template.databaseClient
            .sql("""
                SELECT
                    city.id AS "city.id",
                    city.name AS "city.name",
                    bar.id AS "bar.id",
                    bar.name AS "bar.name",
                    bb.id AS "bar_beer.id",
                    beer.id AS "beer.id",
                    beer.name AS "beer.name",
                    beer.style AS "beer.style"
                FROM
                    city
                LEFT JOIN
                    bar
                ON
                    bar.city_id = city.id
                LEFT JOIN
                    bar_beer AS bb
                ON
                    bb.bar_id = bar.id
                LEFT JOIN
                    beer
                ON
                    bb.beer_id = beer.id
                ORDER BY city.id, bar.id, beer.id
            """.trimIndent())
            .fetch()
            .all()
            .bufferUntilChanged{ result -> result.get("city.id") }
            .map { constructCity(it) }
    }

    private fun constructCity(data: List<Map<String, Any>>): City {
        val head = data.first()
        return City(
            id = head.get("city.id") as Long,
            name = head.get("city.name") as String,
            bars = constructBars(
                data
                    .filter { it.get("bar.id") != null }
                    .groupBy { it.get("bar.id") as Long }
            )
        )
    }

    private fun constructBars(data: Map<Long, List<Map<String, Any>>>): List<Bar> {
        return data
            .values
            .map { constructBar(it) }
            .toList()
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