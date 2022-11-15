package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.Beer
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BeerRepository: ReactiveCrudRepository<Beer, Long>