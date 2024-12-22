package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Decision

interface DecisionService {

    fun getAllDecisions(page: Int, size: Int): List<Decision>

    fun getDecisionById(decisionId: Int): Decision?


    fun saveDecision(decision: Decision): Decision

    fun updateDecision(decision: Decision): Decision

    fun deleteDecision(decisionId: Int): Boolean


}