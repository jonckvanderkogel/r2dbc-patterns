package com.bullet.r2dbcpatterns.dto

import com.bullet.r2dbcpatterns.domain.Beer
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BeerDTO(val id: Long? = null, val name: String, val style: String) {
    companion object {
        fun of(beer: Beer): BeerDTO = BeerDTO(beer.id, beer.name, beer.style.toString())
    }
}

