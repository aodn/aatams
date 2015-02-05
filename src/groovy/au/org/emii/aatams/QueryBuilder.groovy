package au.org.emii.aatams

import org.jooq.*
import org.jooq.conf.ParamType
import org.jooq.impl.DSL

import org.apache.log4j.Logger

import static org.jooq.impl.DSL.*

abstract class QueryBuilder {

    def log = Logger.getLogger(QueryBuilder.class)

    abstract String getViewName(filterParams)

    def constructCountQuery(filterParams) {
        filterParams.count = true

        return constructQuery(filterParams)
    }

    def constructQuery(filterParams)
    {
        DSLContext create = DSL.using(SQLDialect.POSTGRES);

        def query = filterParams.count ?
            create.selectCount() : create.select().limit(filterParams.limit).offset(filterParams.offset)

        query.from(table(getViewName(filterParams)))
        addInClauses(query, filterParams)
        addBetweenClauses(query, filterParams)

        log.debug("query: ${query.getSQL(org.jooq.conf.ParamType.INLINED)}")

        return query
    }

    private void addInClauses(query, filterParams) {
        ["project": filterParams?.filter?.receiverDeployment?.station?.installation?.project?.in?.getAt(1),
         "installation": filterParams?.filter?.receiverDeployment?.station?.installation?.in?.getAt(1),
         "station": filterParams?.filter?.receiverDeployment?.station?.in?.getAt(1),
         "transmitter_id": filterParams?.filter?.in?.getAt(1),
         "spcode": filterParams?.filter?.surgeries?.release?.animal?.species?.in?.getAt(1)
        ].each {
            field, filterValues ->

            if (filterValues) {
                query.where(DSL.fieldByName(field).in(delimitedFilterValuesToList(filterValues)))
            }
        }
    }

    private void addBetweenClauses(query, filterParams) {
        ["timestamp": filterParams?.filter?.between].each
        {
            field, filterValues ->

            if (filterValues)
            {
                assert(filterValues?."1") : "Start date/time must be specified"
                assert(filterValues?."2") : "End date/time must be specified"

                query.where(DSL.fieldByName(field).between(new java.sql.Timestamp(filterValues?."1"?.getTime())).and(new java.sql.Timestamp(filterValues?."2"?.getTime())))
            }
        }
    }

    List delimitedFilterValuesToList(delimVals) {
        return delimVals.tokenize("|").collect { it.trim() }.grep { it }
    }
}
