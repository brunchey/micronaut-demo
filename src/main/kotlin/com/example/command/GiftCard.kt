package com.example.command

import com.example.api.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle

class GiftCard{

    @AggregateIdentifier
    private lateinit var giftCardId: String
    private var remainingValue: Int = 0

    @CommandHandler
    constructor(cmd: IssueCommand) {
        if (cmd.amount <= 0) {
            throw IllegalArgumentException("amount <= 0")
        }

        AggregateLifecycle.apply(IssuedEvent(cmd.id, cmd.amount))
    }

    @CommandHandler
    fun handle(cmd: RedeemCommand) {
        if (cmd.amount <= 0) {
            throw IllegalArgumentException("amount <= 0")
        }

        if (cmd.amount > remainingValue) {
            throw IllegalStateException("amount > remaining value")
        }

        AggregateLifecycle.apply(RedeemedEvent(giftCardId, cmd.amount))
    }

    @CommandHandler
    fun handle(cmd: ReloadCommand) {
        if (cmd.amount <= 0) {
            throw IllegalArgumentException("amount <= 0")
        }

        AggregateLifecycle.apply(ReloadedEvent(giftCardId, cmd.amount))
    }

    @CommandHandler
    fun handle(cmd: CancelCommand) {
        AggregateLifecycle.apply(CancelEvent(giftCardId))
    }

    @EventSourcingHandler
    fun on(event: IssuedEvent) {
        giftCardId = event.id
        remainingValue = event.amount
    }

    @EventSourcingHandler
    fun on(event: RedeemedEvent) {
        remainingValue -= event.amount
    }

    @EventSourcingHandler
    fun on(event: ReloadedEvent) {
        remainingValue += event.amount
    }

    @EventSourcingHandler
    fun on(event: CancelEvent?) {
        remainingValue = 0
    }

    constructor() {
    }
}