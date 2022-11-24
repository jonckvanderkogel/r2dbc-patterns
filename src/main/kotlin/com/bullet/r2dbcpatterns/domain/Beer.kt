package com.bullet.r2dbcpatterns.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("beer")
data class Beer(@Id val id: Long, val name: String, val style: BeerStyle)

enum class BeerStyle {
    LAGER,BLOND,IPA,STOUT,DOUBLE,TRIPLE,BOCK,BITTER,WEIZEN,SOUR,UNKNOWN;

    companion object {
        private val enumMap: Map<String, BeerStyle> = values().associateBy { it.name.lowercase() }
        fun fromString(name: String): BeerStyle = enumMap.getOrDefault(name.lowercase(), UNKNOWN)
    }
}