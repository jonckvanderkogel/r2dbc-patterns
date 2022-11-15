package com.bullet.r2dbcpatterns.dto

import com.bullet.r2dbcpatterns.domain.Bar
import com.bullet.r2dbcpatterns.domain.Beer
import com.bullet.r2dbcpatterns.domain.City
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BarDTO(val id: Long? = null, val name: String, val beers: List<Beer>) {
    companion object {
        fun of(bar: Bar): BarDTO = BarDTO(bar.id, bar.name, bar.beers)
    }
}