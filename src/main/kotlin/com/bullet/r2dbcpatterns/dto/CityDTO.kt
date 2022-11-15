package com.bullet.r2dbcpatterns.dto

import com.bullet.r2dbcpatterns.domain.Bar
import com.bullet.r2dbcpatterns.domain.City
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CityDTO(@Id val id: Long? = null, val name: String, val bars: List<Bar>) {
    companion object {
        fun of(city: City): CityDTO = CityDTO(city.id, city.name, city.bars)
    }
}