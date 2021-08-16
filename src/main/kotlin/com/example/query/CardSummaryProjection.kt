package com.example.query

import com.example.api.*
import io.micronaut.transaction.annotation.TransactionalAdvice
import org.axonframework.eventhandling.EventHandler
import org.axonframework.queryhandling.QueryHandler
import javax.persistence.EntityManager

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

    @QueryHandler
    fun handle(query: FetchCardSummariesQuery): List<CardSummary>? {
        val jpaQuery = entityManager.createNamedQuery(
            "CardSummary.fetch",
            CardSummary::class.java
        )
        jpaQuery.setParameter("idStartsWith", query.filter.idStartsWith)
        jpaQuery.firstResult = query.offset
        jpaQuery.maxResults = query.limit
        val results = jpaQuery.resultList
        return results
    }


}