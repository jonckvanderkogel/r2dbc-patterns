package com.bullet.r2dbcpatterns.domain

import com.bullet.r2dbcpatterns.dbinput.BarBeerInsert
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("bar_beer")
data class BarBeer(@Id val id: Long? = null, val barId: Long, val beerId: Long) {
    companion object {
        fun of(insert: BarBeerInsert): BarBeer = BarBeer(barId = insert.barId, beerId = insert.beerId)
    }
}