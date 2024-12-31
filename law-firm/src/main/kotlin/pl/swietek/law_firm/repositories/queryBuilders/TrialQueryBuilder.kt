package pl.swietek.law_firm.repositories.queryBuilders

class TrialQueryBuilder {
    private val fields = mutableListOf<String>()
    private val joins = mutableListOf<String>()

    fun selectBasic(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "t.id AS trial_id",
                "t.title AS trial_title",
                "t.description AS trial_description",
                "t.trial_status_id AS trial_status_id",
                "t.client_id AS trial_client_id",
                "t.lawyer_id AS trial_lawyer_id",
                "t.judge_id AS trial_judge_id",
                "t.date AS trial_date",
                "t.case_id AS trial_case_id"
            )
        )
        return this
    }

    fun withTrialStatus(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "ts.id AS trial_status_id",
                "ts.name AS trial_status_name"
            )
        )
        joins.add("LEFT JOIN LawFirm.trial_status ts ON t.trial_status_id = ts.id")
        return this
    }

    fun withClient(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "c.id AS client_id",
                "c.first_name AS client_first_name",
                "c.last_name AS client_last_name",
                "c.email AS client_email",
                "c.contact_data_id AS client_contact_data_id"
            )
        )
        joins.add("LEFT JOIN LawFirm.client c ON t.client_id = c.id")
        return this
    }

    fun withLawyer(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "l.id AS lawyer_id",
                "l.first_name AS lawyer_first_name",
                "l.last_name AS lawyer_last_name",
                "l.specialization AS lawyer_specialization"
            )
        )
        joins.add("LEFT JOIN LawFirm.lawyer l ON t.lawyer_id = l.id")
        return this
    }

    fun withJudge(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "j.id AS judge_id",
                "j.first_name AS judge_first_name",
                "j.last_name AS judge_last_name",
                "j.court_division_id AS judge_court_division_id",
                "cd.id AS court_division_id",
                "cd.name AS court_division_name",
                "cd.city AS court_division_city"
            )
        )
        joins.add("LEFT JOIN LawFirm.judge j ON t.judge_id = j.id")
        joins.add("LEFT JOIN LawFirm.court_division cd ON j.court_division_id = cd.id")
        return this
    }

    fun withCase(): TrialQueryBuilder {
        fields.addAll(
            listOf(
                "cs.id AS case_id",
                "cs.name AS case_name",
                "cs.description AS case_description",
                "cs.responsible_lawyer_id AS case_responsible_lawyer_id",
                "cs.client_id AS case_client_id"
            )
        )
        joins.add("LEFT JOIN LawFirm.case cs ON t.case_id = cs.id")
        return this
    }


    fun getFields(): List<String> = fields

    fun getJoins(): List<String> = joins
}
