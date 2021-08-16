package com.example.controllers

import com.example.api.*
import io.micronaut.http.annotation.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway
import java.util.concurrent.CompletableFuture

@Controller("/giftcard")
class GiftCardController(private val commandGateway: CommandGateway,
                         private val queryGateway: QueryGateway) {

    @Get("/summary")
    fun getSummary(): CompletableFuture<List<CardSummary>> {
        val fetchCardSummariesQuery: FetchCardSummariesQuery = FetchCardSummariesQuery(0, 100, CardSummaryFilter(""))
        return queryGateway.query(fetchCardSummariesQuery, ResponseTypes.multipleInstancesOf(CardSummary::class.java))
    }

    @Post("/")
    fun issueCard(@QueryValue id: String, @QueryValue amount: Int) {
        val cmd = IssueCommand(id, amount)
        commandGateway.send<Any>(cmd)
    }

    @Put("/{id}/redeem")
    fun redeemCard(id: String, @QueryValue amount: Int) {
        val cmd = RedeemCommand(id, amount)
        commandGateway.send<Any>(cmd)
    }

    @Put("/{id}/reload")
    fun reloadCard(id: String, @QueryValue amount: Int) {
        val cmd = ReloadCommand(id, amount)
        commandGateway.send<Any>(cmd)
    }

    @Put("/{id}/cancel")
    fun cancelCard(id: String) {
        val cmd = CancelCommand(id)
        commandGateway.send<Any>(cmd)
    }

}