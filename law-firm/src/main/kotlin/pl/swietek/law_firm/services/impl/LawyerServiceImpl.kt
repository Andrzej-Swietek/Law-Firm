package pl.swietek.law_firm.services.impl

import org.springframework.stereotype.Service
import pl.swietek.law_firm.models.Lawyer
import pl.swietek.law_firm.repositories.LawyerRepository
import pl.swietek.law_firm.services.LawyerService

@Service
class LawyerServiceImpl (private val lawyerRepository: LawyerRepository): LawyerService {

    override fun getAllLawyers(): List<Lawyer> = lawyerRepository.getLawyers()

    override fun getLawyerById(lawyerId: Int): Lawyer? = lawyerRepository.getLawyerById(lawyerId)

    override fun saveLawyer(lawyer: Lawyer): Lawyer = lawyerRepository.saveLawyer(lawyer)

    override fun updateLawyer(lawyer: Lawyer): Lawyer = lawyerRepository.updateLawyer(lawyer)

    override fun deleteLawyer(lawyerId: Int): Boolean = lawyerRepository.deleteLawyer(lawyerId)

}