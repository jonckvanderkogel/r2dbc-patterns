package com.bullet.r2dbcpatterns.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("bar_beer")
data class BarBeer(@Id val id: Long? = null, val barId: Long, val beerId: Long)