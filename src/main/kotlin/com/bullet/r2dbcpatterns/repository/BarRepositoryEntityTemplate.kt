package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.dbinput.BarInsert
import com.bullet.r2dbcpatterns.dbinput.BarUpdate
import com.bullet.r2dbcpatterns.domain.Bar
import reactor.core.publisher.Mono

interface BarRepositoryEntityTemplate {
    fun saveBar(insert: BarInsert): Mono<Bar>

    fun updateBar(update: BarUpdate): Mono<Bar>
}