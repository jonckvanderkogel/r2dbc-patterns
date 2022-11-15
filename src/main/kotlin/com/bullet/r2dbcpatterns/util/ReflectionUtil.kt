package com.bullet.r2dbcpatterns.util

import com.fasterxml.jackson.databind.ObjectMapper
import reactor.core.publisher.Mono

class ReflectionUtil {
    companion object {
        private val objectMapper = ObjectMapper()

        fun <T : Any> deserializeToPojo(map: Map<String, Any>, clazz: Class<T>): Mono<T> {
            return try {
                Mono.just(objectMapper.convertValue(map, clazz))
            } catch (ex: Exception) {
                Mono.error(ex)
            }
        }
    }
}