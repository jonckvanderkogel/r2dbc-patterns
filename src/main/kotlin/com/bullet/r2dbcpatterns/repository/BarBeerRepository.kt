package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.BarBeer
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BarBeerRepository: ReactiveCrudRepository<BarBeer, Long>, BarBeerRepositoryEntityTemplate