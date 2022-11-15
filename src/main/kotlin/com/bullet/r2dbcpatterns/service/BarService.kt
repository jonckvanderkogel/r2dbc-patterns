package com.bullet.r2dbcpatterns.service

import com.bullet.r2dbcpatterns.dbinput.BarInsert
import com.bullet.r2dbcpatterns.dbinput.BarUpdate
import com.bullet.r2dbcpatterns.domain.Bar
import com.bullet.r2dbcpatterns.repository.BarRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BarService(val barRepository: BarRepository) {
    fun save(insert: BarInsert): Mono<Bar> = barRepository.saveBar(insert)

    fun update(update: BarUpdate): Mono<Bar> = barRepository.updateBar(update)
}