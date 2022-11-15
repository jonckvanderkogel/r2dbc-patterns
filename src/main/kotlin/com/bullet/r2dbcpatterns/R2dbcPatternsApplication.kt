package com.bullet.r2dbcpatterns

import com.bullet.r2dbcpatterns.postgres.DataSourceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(DataSourceProperties::class)
@SpringBootApplication
class R2dbcPatternsApplication

fun main(args: Array<String>) {
    runApplication<R2dbcPatternsApplication>(*args)
}
