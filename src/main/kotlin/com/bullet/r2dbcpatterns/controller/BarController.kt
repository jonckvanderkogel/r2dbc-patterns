package com.bullet.r2dbcpatterns.controller

import com.bullet.r2dbcpatterns.dbinput.BarInsert
import com.bullet.r2dbcpatterns.dbinput.BarUpdate
import com.bullet.r2dbcpatterns.dto.BarDTO
import com.bullet.r2dbcpatterns.service.BarService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bar")
class BarController(val barService: BarService) {
    /*
    curl -H "Content-Type: application/json" -X POST -d '{"name":"Frontaal Bar", "cityId": 1}' http://localhost:8080/bar
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody insert: BarInsert): Mono<BarDTO> = barService
        .save(insert)
        .map { BarDTO.of(it) }

    /*
    curl -H "Content-Type: application/json" -X PUT -d '{"id": 1, "name":"Frontaal Bar", "cityId": 2}' http://localhost:8080/bar
     */
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody update: BarUpdate): Mono<BarDTO> = barService
        .update(update)
        .map { BarDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("Could not update bar with id ${update.id}")))

    /*
    curl "http://localhost:8080/bar?id=1"
     */
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestParam("id") id: Long): Mono<BarDTO> = barService
        .get(id)
        .map { BarDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No bar with id $id")))

    /*
    curl "http://localhost:8080/bar/all"
     */
    @GetMapping(path = ["/all"])
    fun getAll(): Flux<BarDTO> = barService
        .getAll()
        .map { BarDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No bar entities in database")))

    /*
    curl -X DELETE "http://localhost:8080/bar?id=1"
     */
    @DeleteMapping
    fun delete(@RequestParam("id") id: Long): Mono<String> = barService
        .delete(id)
        .then(Mono.just("{\"message\":\"bar with $id deleted\"}"))
}