package com.bullet.r2dbcpatterns.postgres

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.testcontainers.containers.PostgreSQLContainer


@Configuration
class DataSourceConfig(private val dataSourceProperties: DataSourceProperties) : AbstractR2dbcConfiguration() {
    companion object {
        const val CONNECTION_FACTORY = "postgresConnectionFactory"
        const val POSTGRES_BEAN = "postgresLocal"
        private val logger = LoggerFactory.getLogger(DataSourceConfig::class.java)
    }

    @Bean(name = [POSTGRES_BEAN])
    fun createPostgreSQLContainer(): PostgreSQLContainer<*> {
        logger.info("Starting PostgreSQL container")

        val postgreSQLContainer = PostgreSQLContainer("postgres:15.1")
            .withDatabaseName(dataSourceProperties.database)
            .withUsername(dataSourceProperties.username)
            .withPassword(dataSourceProperties.password)
            .withExposedPorts(dataSourceProperties.port)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(
                    HostConfig().withPortBindings(
                        PortBinding(
                            Ports.Binding.bindPort(dataSourceProperties.port),
                            ExposedPort(dataSourceProperties.port)
                        )
                    )
                )
            }

        postgreSQLContainer.start()

        logger.info("Started PostgreSQL on port: ${postgreSQLContainer.firstMappedPort}")

        return postgreSQLContainer
    }

    @Bean(name = [CONNECTION_FACTORY])
    @DependsOn(POSTGRES_BEAN)
    override fun connectionFactory(): ConnectionFactory {
        return PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(dataSourceProperties.host)
                .username(dataSourceProperties.username)
                .password(dataSourceProperties.password)
                .database(dataSourceProperties.database)
                .port(dataSourceProperties.port)
                .build()
        )
    }
}