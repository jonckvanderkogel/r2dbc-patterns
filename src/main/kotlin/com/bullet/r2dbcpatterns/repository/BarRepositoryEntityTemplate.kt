package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.messages.BarInsert
import com.bullet.r2dbcpatterns.messages.BarUpdate
import com.bullet.r2dbcpatterns.domain.Bar
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface BarRepositoryEntityTemplate {
    fun saveBar(insert: BarInsert): Mono<Bar>

    fun get(id: Long): Mono<Bar>

    fun getAll(): Flux<Bar>

    fun updateBar(update: BarUpdate): Mono<Bar>
}