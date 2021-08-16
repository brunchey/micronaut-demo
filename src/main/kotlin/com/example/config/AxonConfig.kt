package com.example.config

import com.example.command.GiftCard
import com.example.query.CardSummaryProjection
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.annotation.TransactionalAdvice
import io.micronaut.transaction.hibernate5.HibernateTransactionManager
import io.micronaut.transaction.hibernate5.MicronautSessionContext
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.common.jpa.SimpleEntityManagerProvider
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.config.Configuration
import org.axonframework.config.Configurer
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.messaging.Message
import org.axonframework.messaging.interceptors.LoggingInterceptor
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import org.axonframework.serialization.xml.XStreamSerializer
import javax.annotation.PreDestroy
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.SynchronizationType


@Factory
@TransactionalAdvice
class AxonConfig {

    @Context
    fun startUp(cardSummaryProjection: CardSummaryProjection,
                entityManager: EntityManager,
                serializer: Serializer,
                txManager: TransactionManager):Configuration {

        val configurer = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard::class.java)
                .configureTransactionManager { _ -> txManager }
                .registerMessageHandler{ _ -> cardSummaryProjection }

        configurer.configureSerializer { _ -> serializer}
        configurer.configureMessageSerializer { _ -> XStreamSerializer.defaultSerializer() }
        configurer.eventProcessing().registerTokenStore { _ -> tokenStore(entityManager, serializer)}

        return configurer.start()
    }

    @Bean
    fun commandGateway(config: Configuration) : CommandGateway {
        return config.commandGateway()
    }

    @Bean
    fun queryGateway(config: Configuration) : QueryGateway {
        return config.queryGateway()
    }

    @Bean
    fun serializer(objectMapper: ObjectMapper): Serializer {
        return JacksonSerializer.builder()
            .objectMapper(objectMapper)
            .build()
    }

    @Bean
    fun objectMapper():ObjectMapper {
        return ObjectMapper().registerModule(KotlinModule())
    }

    @Bean
    fun txManager(txManager: HibernateTransactionManager): TransactionManager {
        return JpaTransactionManager(txManager)
    }

    fun tokenStore(entityManager: EntityManager, serializer: Serializer) : TokenStore {
        return JpaTokenStore.builder()
                .serializer(serializer)
                .entityManagerProvider(SimpleEntityManagerProvider(entityManager))
                .build()
    }
}