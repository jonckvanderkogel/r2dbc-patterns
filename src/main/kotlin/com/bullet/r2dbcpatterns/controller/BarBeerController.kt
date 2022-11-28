package com.bullet.r2dbcpatterns.controller

import com.bullet.r2dbcpatterns.messages.BarBeerInsert
import com.bullet.r2dbcpatterns.dto.BarBeerDTO
import com.bullet.r2dbcpatterns.dto.BarDTO
import com.bullet.r2dbcpatterns.service.BarBeerService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/barbeer")
class BarBeerController(val barBeerService: BarBeerService) {
    /*
    curl -H "Content-Type: application/json" -X POST -d '{"barId":1, "beerId": 1}' http://localhost:8080/barbeer
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody insert: BarBeerInsert): Mono<BarBeerDTO> = barBeerService
        .save(insert)
        .map { BarBeerDTO.of(it) }

    /*
    curl "http://localhost:8080/barbeer?id=1"
     */
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestParam("id") id: Long): Mono<BarBeerDTO> = barBeerService
        .get(id)
        .map { BarBeerDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No BarBeer with id $id")))

    /*
    curl -X DELETE "http://localhost:8080/barbeer?id=1"
     */
    @DeleteMapping
    fun delete(@RequestParam("id") id: Long): Mono<String> = barBeerService
        .delete(id)
        .then(Mono.just("{\"message\":\"bar-beer with $id deleted\"}"))
}