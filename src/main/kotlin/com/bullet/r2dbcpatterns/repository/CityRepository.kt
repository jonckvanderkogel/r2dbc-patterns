package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.City
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface CityRepository: ReactiveCrudRepository<City, Long>, CityRepositoryEntityTemplate