package pl.swietek.law_firm.services

import pl.swietek.law_firm.models.Lawyer

interface LawyerService {
    fun getAllLawyers(): List<Lawyer>

    fun getLawyerById(lawyerId: Int): Lawyer?

    fun saveLawyer(lawyer: Lawyer): Lawyer

    fun updateLawyer(lawyer: Lawyer): Lawyer

    fun deleteLawyer(lawyerId: Int): Boolean
}