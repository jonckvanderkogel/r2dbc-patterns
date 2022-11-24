package com.bullet.r2dbcpatterns.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("bar")
data class Bar(@Id val id: Long, val name: String, val beers: List<Beer>)