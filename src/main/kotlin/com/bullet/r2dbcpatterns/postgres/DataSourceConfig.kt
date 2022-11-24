package com.bullet.r2dbcpatterns.postgres

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.testcontainers.containers.PostgreSQLContainer


@Profile("live")
@Configuration
class DataSourceConfig(
    @Value("\${datasource.database}")private val  database: String,
    @Value("\${datasource.username}")private val  username: String,
    @Value("\${datasource.password}")private val  password: String,
    @Value("\${datasource.port}")private val  port: Int
) {
    companion object {
        const val POSTGRES_BEAN = "postgresLocal"
        private val logger = LoggerFactory.getLogger(DataSourceConfig::class.java)
    }

    @Bean(name = [POSTGRES_BEAN])
    fun createPostgreSQLContainer(): PostgreSQLContainer<*> {
        logger.info("Starting PostgreSQL container")

        val postgreSQLContainer = PostgreSQLContainer("postgres:15.1")
            .withDatabaseName(database)
            .withUsername(username)
            .withPassword(password)
            .withExposedPorts(port)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(
                            Ports.Binding.bindPort(port),
                            ExposedPort(port)
                        )
                    )
                )
            }

        postgreSQLContainer.start()

        logger.info("Started PostgreSQL on port: ${postgreSQLContainer.firstMappedPort}")

        return postgreSQLContainer
    }
}