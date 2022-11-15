package com.bullet.r2dbcpatterns.domain

import com.bullet.r2dbcpatterns.dbinput.BeerInput
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("beer")
data class Beer(@Id val id: Long? = null, val name: String, val style: BeerStyle) {
    companion object {
        fun of(beerInput: BeerInput): Beer = Beer(beerInput.id, beerInput.name, BeerStyle.fromString(beerInput.style))
    }
}

enum class BeerStyle {
    LAGER,BLOND,IPA,STOUT,DOUBLE,TRIPLE,BOCK,BITTER,WEIZEN,SOUR,UNKNOWN;

    companion object {
        private val enumMap: Map<String, BeerStyle> = values().associateBy { it.name.lowercase() }
        fun fromString(name: String): BeerStyle = enumMap.getOrDefault(name.lowercase(), UNKNOWN)
    }
}