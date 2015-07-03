package au.org.emii.aatams.filter

import au.org.emii.aatams.PermissionUtilsService
import au.org.emii.aatams.Project
import au.org.emii.aatams.ProjectRole
import au.org.emii.aatams.report.ReportInfoService
import org.hibernate.criterion.Restrictions

import java.text.DateFormat
import java.text.SimpleDateFormat

class QueryService {
    static transactional = false

    def visibilityControlService

    Map<Long, Collection> query(clazz, params) {
        return query(clazz, params, false)
    }

    /**
     * Return map:
     *
     * count, rows
     *
     * Important note: skipVisibilityControlChecks should only be set to true
     * when you are certain that the returned data will not expose
     * any possible embargoed or protected information (e.g. the sensor track
     * KML export).
     *
     * It is provided purely for performance reasons.
     *
     * @return
     */
    Map<Long, Collection> query(clazz, params, skipVisibilityControlChecks) {
        return [
            results: queryWithoutCount(clazz, params, skipVisibilityControlChecks)['results'],
            count: queryCountOnly(clazz, params)
        ]
    }

    Map<Long, Collection> queryWithoutCount(clazz, params) {
        return queryWithoutCount(clazz, params, false)
    }

    Map<Long, Collection> queryWithoutCount(clazz, params, skipVisibilityControlChecks) {
        def results = []

        if (hasFilter(params)) {
            def criteria = clazz.createCriteria()
            criteria.getInstance()?.setCacheable(true)

            def transformedParams = transformParams(params)

            log.debug("transformed params: " + transformedParams)

            results = criteria.list(transformedParams, buildCriteriaClosure(transformedParams.filter))
        }
        else {
            results = clazz.list(params)
        }

        if (!skipVisibilityControlChecks) {
            results = visibilityControlService.applyVisibilityControls(results)
        }

        return [results: results]
    }

    Number queryCountOnly(clazz, params) {
        if (hasFilter(params)) {
            def criteria = clazz.createCriteria()
            criteria.getInstance()?.setCacheable(true)

            def transformedParams = transformParams(removeSortOrderParams(params))

            log.debug("transformed params: " + transformedParams)

            return criteria.count(buildCriteriaClosure(transformedParams.filter))
        }

        return clazz.count()
    }

    def hasFilter(params) {
        return !(!params || params.isEmpty() || params?.filter == null || params?.filter == [:])
    }

    private Closure buildCriteriaClosure(Map params) {
        return ( {
            params?.each {
                method, nestedParams ->

                if (method == "isNull") {
                    // Doesn't work, see: http://community.jboss.org/wiki/HibernateFAQ-AdvancedProblems#The_query_language_IS_NULL_syntax_wont_work_with_a_onetoone_association
                    // criteria.add(Restrictions.isNull(property))
                    criteria.add(Restrictions.sqlRestriction("recoverer_id IS NULL"))
                }
                else if (method == "in") {
                    assert(nestedParams.size() == 2): "Invalid nested params: " + nestedParams
                    def parsedParams = []
                    parsedParams.add(nestedParams[0])
                    parsedParams.add(
                        nestedParams[1].tokenize("|").collect {
                            it.trim()
                        }.grep {
                            it
                        }
                    )
                    invokeMethod(method, parsedParams as Object[])
                }
                else if ((method == "eq") && (nestedParams as List).contains(ReportInfoService.MEMBER_PROJECTS)) {
                    assert(nestedParams.size() == 2): "Invalid nested params: " + nestedParams
                    assert(nestedParams[1] == ReportInfoService.MEMBER_PROJECTS): "Invalid nested params: " + nestedParams

                    def projectNames = memberProjects?.collect { it[nestedParams[0]] }
                    if (!projectNames.isEmpty()) {
                        invokeMethod("in", [nestedParams[0], projectNames] as Object[])
                    }
                    else {
                        invokeMethod("in", [nestedParams[0], ["Garbage value"]] as Object[])
                    }
                }
                else if (nestedParams instanceof List) {
                    invokeMethod(method, nestedParams as Object[])
                }
                else if (nestedParams instanceof Map) {
                    return invokeMethod(method, buildCriteriaClosure(nestedParams))
                }
                else if (nestedParams.class.isArray()) {
                    invokeMethod(method, nestedParams)
                }
                else {
                    assert(false) : "Invalid params: " + nestedParams + ", class: " + nestedParams.class
                }
            }
        })
    }

    private Map transformParams(params) {
        def transformedParams = cleanParams(params)
        if (transformedParams.filter == null) {
            transformedParams.filter = [:]
        }

        def sortKey = transformedParams.remove("sort")
        def order = transformedParams.remove("order")

        if (sortKey && order) {
            def targetMap = transformedParams.filter
            def tokens = sortKey.tokenize(".")
            def sortParam = tokens.pop()

            tokens.each {
                def nestedTargetMap = targetMap[it]

                // This is possible when there is a sort but no restriction.
                if (!nestedTargetMap) {
                    nestedTargetMap = [:]
                    targetMap[it] = nestedTargetMap
                }

                targetMap = nestedTargetMap
            }

            assert(targetMap != null)
            targetMap.put("order", [sortParam, order])
        }

        return transformedParams
    }

    private void cleanDateParams(params) {
        [1, 2].each {
            if (params["filter.between." + it] && params["filter.between." + it].class == String) {
                // Thu Jun 18 12:38:00 EST 2009
                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
                def dateAsString = params["filter.between." + it]

                params["filter.between." + it] = dateFormat.parse(dateAsString)
                params?.filter["between." + it] = dateFormat.parse(dateAsString)
            }
        }
    }

    private Map cleanParams(params) {
        cleanDateParams(params)

        def cleanedParams = [:]

        params.each {
            k, v ->

            // Make life easier by dealing only with lists (not arrays also).
            if (v?.class?.isArray()) {
                v = v.toList()
            }

            if (k == "filter") {
                if (v?.between) {
                    v.between = []
                    [0, 1, 2].each {
                        assert(v["between." + it]): "Invalid \"between\" filter."
                        v.between.add(v["between." + it])
                    }
                }
            }

            if (k.contains(".")) {

            }
            else if (k.endsWith("_year") || k.endsWith("_month") || k.endsWith("_day") || k.endsWith("_hour") || k.endsWith("_minute") || k.endsWith("_second")) {
                // Ignore - a proper date struct will be in the map elsewhere.
            }
            else if (v instanceof Map) {
                def subParams = cleanParams(v)
                if (subParams != [:]) {
                    cleanedParams.put(k, subParams)
                }
            }
            else if (v instanceof List) {
                if (v.isEmpty() || v.contains("") || v.contains(null)) {
                }
                else {
                    cleanedParams.put(k, v)
                }
            }
            else if (v instanceof String) {
                if (!v.isEmpty()) {
                    cleanedParams.put(k, v)
                }
            }
            else {
                cleanedParams.put(k, v)
            }
        }

        return cleanedParams
    }

    private List<Project> getMemberProjects() {
        def roles = ProjectRole.findAllByPerson(PermissionUtilsService.principal())
        return roles*.project
    }

    private removeSortOrderParams(params) {
        def paramsCopy = params.clone()
        paramsCopy.remove("sort")
        paramsCopy.remove("order")

        return paramsCopy
    }
}
