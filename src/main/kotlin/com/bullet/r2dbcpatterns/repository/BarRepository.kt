package com.bullet.r2dbcpatterns.repository

import com.bullet.r2dbcpatterns.domain.Bar
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BarRepository: ReactiveCrudRepository<Bar, Long>, BarRepositoryEntityTemplate