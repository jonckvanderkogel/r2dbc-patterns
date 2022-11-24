package com.bullet.r2dbcpatterns.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("city")
data class City(@Id val id: Long, val name: String, val bars: List<Bar>)