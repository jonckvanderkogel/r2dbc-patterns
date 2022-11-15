package com.bullet.r2dbcpatterns.postgres

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "datasource")
data class DataSourceProperties (
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String
)