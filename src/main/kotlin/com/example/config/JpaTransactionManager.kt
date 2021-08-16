package com.example.config

import io.micronaut.transaction.TransactionDefinition
import io.micronaut.transaction.TransactionStatus
import io.micronaut.transaction.hibernate5.HibernateTransactionManager
import io.micronaut.transaction.support.DefaultTransactionDefinition
import org.axonframework.common.transaction.Transaction
import org.axonframework.common.transaction.TransactionManager

class JpaTransactionManager(private val txManager: HibernateTransactionManager) : TransactionManager {

    private val txDefinition: TransactionDefinition = DefaultTransactionDefinition()

    override fun startTransaction(): Transaction {
        val status: TransactionStatus<*> = this.txManager.getTransaction(this.txDefinition)
        return object : Transaction {
            override fun commit() {
                this@JpaTransactionManager.commitTransaction(status)
            }

            override fun rollback() {
                this@JpaTransactionManager.rollbackTransaction(status)
            }
        }
    }

    protected fun commitTransaction(status: TransactionStatus<*>) {
        if (status.isNewTransaction && !status.isCompleted) {
            this.txManager.commit(status)
        }
    }

    protected fun rollbackTransaction(status: TransactionStatus<*>) {
        if (status.isNewTransaction && !status.isCompleted) {
            this.txManager.rollback(status)
        }
    }

}