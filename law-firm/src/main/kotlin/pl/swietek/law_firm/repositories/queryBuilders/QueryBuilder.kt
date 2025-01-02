package pl.swietek.law_firm.repositories.queryBuilders

abstract class QueryBuilder {
    private val fields = mutableListOf<String>()
    private val joins = mutableListOf<String>()

    private val conditions = mutableListOf<String>()
    private val groupBys = mutableListOf<String>()
    private val orderBys = mutableListOf<String>()

    abstract fun build(): String

    fun select(fields: List<String>): QueryBuilder {
        this.fields.clear()
        this.joins.addAll(fields)
        return this
    }

    fun select(field: String): QueryBuilder {
        fields.add("$field, ")
        return this
    }

    fun selectMore(fields: List<String>): QueryBuilder {
        this.fields.addAll(fields)
        return this
    }

    fun join(joinClause: String): QueryBuilder {
        joins.add(joinClause)
        return this
    }

    fun joinMore(joinClauses: List<String>): QueryBuilder {
        joins.addAll(joinClauses)
        return this
    }

    fun where(condition: String): QueryBuilder {
        conditions.clear()
        conditions.add(condition)
        return this
    }

    fun andWhere(condition: String): QueryBuilder {
        conditions.add(condition)
        return this
    }

    fun orWhere(condition: String): QueryBuilder {
        if (conditions.isNotEmpty()) {
            val lastCondition = conditions.removeLast()
            conditions.add("($lastCondition OR $condition)")
        } else {
            conditions.add(condition)
        }
        return this
    }

    fun groupBy(field: String): QueryBuilder {
        groupBys.add(field)
        return this
    }

    fun groupByMore(fields: List<String>): QueryBuilder {
        groupBys.addAll(fields)
        return this
    }

    fun orderBy(field: String, direction: String = "ASC"): QueryBuilder {
        orderBys.add("$field $direction")
        return this
    }

    fun orderByMore(fields: List<Pair<String, String>>): QueryBuilder {
        fields.forEach { (field, direction) ->
            orderBys.add("$field $direction")
        }
        return this
    }

    open fun getFields(): List<String> = fields
    open fun getJoins(): List<String> = joins

    open fun getConditions(): List<String> = conditions
    open fun getGroupBys(): List<String> = groupBys
    open fun getOrderBys(): List<String> = orderBys

    protected fun assembleQuery(baseTable: String): String {
        val selectClause = if (fields.isNotEmpty()) "SELECT ${fields.joinToString(", ")}" else "SELECT *"
        val fromClause = "FROM $baseTable"
        val joinClause = if (joins.isNotEmpty()) joins.joinToString(" ") else ""
        val whereClause = if (conditions.isNotEmpty()) "WHERE ${conditions.joinToString(" AND ")}" else ""
        val groupByClause = if (groupBys.isNotEmpty()) "GROUP BY ${groupBys.joinToString(", ")}" else ""
        val orderByClause = if (orderBys.isNotEmpty()) "ORDER BY ${orderBys.joinToString(", ")}" else ""

        return listOf(selectClause, fromClause, joinClause, whereClause, groupByClause, orderByClause)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }
}