package com.example.query

import com.example.api.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.transaction.annotation.TransactionalAdvice
import org.axonframework.eventhandling.EventHandler
import javax.persistence.EntityManager
import javax.transaction.Transactional

@TransactionalAdvice
class CardSummaryProjection(val entityManager: EntityManager) {

    @EventHandler
    fun on(event: IssuedEvent) {
        entityManager.persist(CardSummary(event.id, event.amount, event.amount))
    }

    @EventHandler
    fun on(event: RedeemedEvent) {
        val cardSummary = entityManager.find(CardSummary::class.java, event.id)
        cardSummary.remainingValue -= event.amount
    }

    @EventHandler
    fun on(event: ReloadedEvent) {
        val cardSummary = entityManager.find(CardSummary::class.java, event.id)
        cardSummary.remainingValue += event.amount
    }

    @EventHandler
    fun on(event: CancelEvent) {
        val cardSummary = entityManager.find(CardSummary::class.java, event.id)
        cardSummary.remainingValue = 0
    }

}