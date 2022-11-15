package com.bullet.r2dbcpatterns.controller

import com.bullet.r2dbcpatterns.dbinput.CityInsert
import com.bullet.r2dbcpatterns.dbinput.CityUpdate
import com.bullet.r2dbcpatterns.dto.CityDTO
import com.bullet.r2dbcpatterns.service.CityService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/city")
class CityController(private val cityService: CityService) {
    /*
    curl -H "Content-Type: application/json" -X POST -d '{"name":"Amsterdam"}' http://localhost:8080/city
     */
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun save(@RequestBody input: CityInsert): Mono<CityDTO> = cityService
        .save(input)
        .map { CityDTO.of(it) }

    /*
    curl -H "Content-Type: application/json" -X PUT -d '{"id": 1, "name":"Berlin"}' http://localhost:8080/city
     */
    @PutMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun update(@RequestBody update: CityUpdate): Mono<CityDTO> = cityService
        .update(update)
        .map { CityDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("Could not update city with id ${update.id}")))

    /*
    curl "http://localhost:8080/city?id=1"
     */
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun get(@RequestParam("id") id: Long): Mono<CityDTO> = cityService
        .get(id)
        .map { CityDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No city with id $id")))

    /*
    curl "http://localhost:8080/city/all"
     */
    @GetMapping(path = ["/all"])
    fun getAll(): Flux<CityDTO> = cityService
        .getAll()
        .map { CityDTO.of(it) }
        .switchIfEmpty(Mono.error(IllegalArgumentException("No city entities in database")))

    /*
    curl -X DELETE "http://localhost:8080/city?id=1"
     */
    @DeleteMapping
    fun delete(@RequestParam("id") id: Long): Mono<String> = cityService
        .delete(id)
        .then(Mono.just("{\"message\":\"city with $id deleted\"}"))
}