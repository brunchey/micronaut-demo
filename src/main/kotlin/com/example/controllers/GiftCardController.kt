package com.example.controllers

import com.example.api.*
import io.micronaut.http.annotation.*
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import javax.persistence.EntityManager


@Controller("/giftcard")
class GiftCardController(private val commandGateway: CommandGateway) {

//    @Get("/summary")
//    fun getSummary(): List<CardSummary> {
//
//    }

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