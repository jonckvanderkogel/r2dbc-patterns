package com.bullet.r2dbcpatterns.dto


import com.bullet.r2dbcpatterns.domain.BarBeer
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BarBeerDTO(val barId: Long, val beerId: Long) {
    companion object {
        fun of(barBeer: BarBeer): BarBeerDTO = BarBeerDTO(barBeer.barId, barBeer.beerId)
    }
}