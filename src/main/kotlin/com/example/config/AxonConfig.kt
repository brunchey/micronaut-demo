package com.example.config

import com.example.command.GiftCard
import com.example.query.CardSummaryProjection
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.transaction.annotation.TransactionalAdvice
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.config.Configuration
import org.axonframework.config.Configurer
import org.axonframework.config.DefaultConfigurer
import org.axonframework.config.EventProcessingConfigurer
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore
import org.axonframework.queryhandling.QueryGateway
import org.axonframework.serialization.Serializer
import org.axonframework.serialization.json.JacksonSerializer
import javax.annotation.PreDestroy
import javax.inject.Singleton
import javax.persistence.EntityManager


@Factory
@TransactionalAdvice
class AxonConfig {

    @Bean
    fun startUp(cardSummaryProjection: CardSummaryProjection,
                entityManager: EntityManager,
                serializer: Serializer):Configuration {

        val configurer = DefaultConfigurer.defaultConfiguration()
                .configureAggregate(GiftCard::class.java)
                .registerMessageHandler{ _ -> cardSummaryProjection }

        configurer.configureSerializer { _ -> serializer}

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

    //@Bean
    fun tokenStore(entityManager: EntityManager, serializer: Serializer) : TokenStore {
        return JpaTokenStore.builder()
                .serializer(serializer)
                .entityManagerProvider {entityManager}
                .build()
    }

    @Bean
    fun serializer(): Serializer {
        return JacksonSerializer.defaultSerializer()
    }
}