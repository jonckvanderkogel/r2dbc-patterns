package com.bullet.r2dbcpatterns.controller

import com.bullet.r2dbcpatterns.domain.BeerStyle
import com.bullet.r2dbcpatterns.dto.BeerDTO
import com.bullet.r2dbcpatterns.messages.BeerInsert
import com.bullet.r2dbcpatterns.messages.BeerUpdate
import com.bullet.r2dbcpatterns.service.BeerService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/beer")
class BeerController(val beerService: BeerService) {

    /*
    curl -H "Content-Type: application/json" -X POST -d '{"name":"Punk IPA", "style":"IPA"}' http://localhost:8080/beer
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody insert: BeerInsert): Mono<BeerDTO> = beerService
        .save(insert)
        .map { BeerDTO.of(it) }

    /*
    curl -H "Content-Type: application/json" -X PUT -d '{"id": 1, "name":"Leffe", "style":"BLOND"}' http://localhost:8080/beer
     */
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody update: BeerUpdate): Mono<BeerDTO> = beerService
        .update(update)
        .map { BeerDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("Could not update beer with id ${update.id}")))

    /*
    curl "http://localhost:8080/beer?id=1"
     */
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestParam("id") id: Long): Mono<BeerDTO> = beerService
        .get(id)
        .map { BeerDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No beer with id $id")))

    /*
    curl "http://localhost:8080/beer/all"
     */
    @GetMapping(path = ["/all"])
    fun getAll(): Flux<BeerDTO> = beerService
        .getAll()
        .map { BeerDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No beer entities in database")))

    /*
    curl "http://localhost:8080/beer/allByStyle?style=IPA"
     */
    @GetMapping(path = ["/allByStyle"])
    fun getAllByStyle(style: BeerStyle): Flux<BeerDTO> = beerService
        .findAllByStyle(style)
        .map { BeerDTO.of(it) }

    /*
    curl "http://localhost:8080/beer/countNonAssociatedBeers"
     */
    @GetMapping(path = ["/countNonAssociatedBeers"])
    fun countNonAssociatedBeers(): Mono<Int> = beerService
        .getCountOfBeersNotAssociatedWithBar()

    /*
    curl -X DELETE "http://localhost:8080/beer?id=1"
     */
    @DeleteMapping
    fun delete(@RequestParam("id") id: Long): Mono<String> = beerService
        .delete(id)
        .then(Mono.just("{\"message\":\"beer with $id deleted\"}"))
}