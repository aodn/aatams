package au.org.emii.aatams.detection

import au.org.emii.aatams.report.ReportController
import groovy.sql.Sql
import java.text.DateFormat
import java.text.SimpleDateFormat

class DetectionController extends ReportController
{
    def candidateEntitiesService
    def dataSource
    def detectionExtractService

    static allowedMethods = [update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list =
    {
//        if (queryService.hasFilter(params)) {
//            def countParams = params.clone()
//            def clazz = reportInfoService.getClassForName("detection")
//
//            params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
//
//            def resultList = queryService.queryWithoutCount(clazz, params, false)
//
//            countParams.max = grailsApplication.config.detection.filter.count.max + 1
//            def count = queryService.queryCountOnly(clazz, countParams)
//
//            flattenParams()
//
//            if (count < countParams.max) {
//                flash.message = "${count} matching records (${clazz.count()} total)."
//            }
//            else {
//                flash.message = "&gt; ${grailsApplication.config.detection.filter.count.max} matching records (${clazz.count()} total)."
//                count = count - 1
//            }
//            return [entityList: resultList.results, total: count]
//        }
//        else {
//            println "#NOFILTER"

        def speciesFilter = params.filter?.animal?.species

        if (speciesFilter) {
            println "Has species filter:- $speciesFilter"

            println "So how do we behave differently?"

            println "DAVID, CONTINUE HERE!!!!"
        }

        def result = doList("detection")

        def el = result.entityList
        println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"
        println "el: (${el.getClass()})"
        println el.join("\n")
        println "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"

        flattenParams()
        return result
//        }
    }

    protected void cleanDateParams()
    {
        [1, 2].each
        {
            if (params["filter.between." + it] && params["filter.between." + it].class == String)
            {
                // Thu Jun 18 12:38:00 EST 2009
                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
                def dateAsString = params["filter.between." + it]
                Date parsedDate = dateFormat.parse(dateAsString)

                params["filter.between." + it] = parsedDate
                params.filter["between." + it] = parsedDate
                params.filter.between."${it}" = parsedDate
            }
        }
    }

    protected def getResultList(queryName)
    {
        params.sql = new Sql(dataSource)
        params.projectPermissionCache = [:]
        params.limit = params.max

        if (params.limit) {
            params.limit = Integer.valueOf(params.limit)
        }

        if (params.offset) {
            params.offset = Integer.valueOf(params.offset)
        }

        // The data params get turned in to strings on the way back to front-end - need to change
        // them back to java.util.Dates again.
        cleanDateParams()


        _checkSpeciesFilter()
//        def detections = detectionExtractService.applyEmbargo(detectionExtractService.extractPage(params), params) // Todo - DN: I wonder if we need to bother with applyEmbargo() here?
        def detections = detectionExtractService.extractPage(params, true)
//        def detections = detectionExtractService.extractPage(params)/*.grep { it }*/

        def previousSize = detections.size()

        detections = detections.collect {
            def validDetection = ValidDetection.get(it.detection_id)

            def retVal = detectionExtractService.hasReadPermission(validDetection.project.id, params) ? validDetection : validDetection.applyEmbargo(params.allowSanitisedResults)
            println "retVal: $retVal"
            retVal
        }.findAll { it != null }

        println "detections.size() = $previousSize -> ${detections.size()}"

        def paramsClone = params.clone()

        paramsClone.max = grailsApplication.config.filter.count.max + 1
        def count = detectionExtractService.getCount(paramsClone)

        params.remove("sql")
        params.remove("projectPermissionCache")

        [results: detections, count: count]
    }

    def export =
    {
        _checkSpeciesFilter()

        if (['KMZ', 'KMZ (tag tracks)', 'KMZ (bubble plot)'].contains(params._action_export))
        {
            //            doExport("detection")
            assert(false): "This is functionality is currently disabled, in order to implement to 'Protected Species' feature."
        }
        else
        {
            detectionExtractService.generateReport(params, request, response)
        }
    }

    def create =
    {
        redirect(controller:"receiverDownloadFile",
                 action:"createDetections")
    }

    def show = {
        def detectionInstance = ValidDetection.get(params.id)
        if (!detectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'ValidDetection'), params.id])}"
            redirect(action: "list")
        }
        else {
            [detectionInstance: detectionInstance]
        }
    }

    def edit = {
        def detectionInstance = ValidDetection.get(params.id)
        if (!detectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'ValidDetection'), params.id])}"
            redirect(action: "list")
        }
        else
        {
            def model = [detectionInstance: detectionInstance]
            model.candidateDeployments = candidateEntitiesService.deployments()
            return model
        }
    }

    def update = {
        def detectionInstance = ValidDetection.get(params.id)
        if (detectionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (detectionInstance.version > version) {

                    detectionInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'detection.label', default: 'ValidDetection')] as Object[], "Another user has updated this ValidDetection while you were editing")
                    render(view: "edit", model: [detectionInstance: detectionInstance])
                    return
                }
            }
            detectionInstance.properties = params
            if (!detectionInstance.hasErrors() && detectionInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'detection.label', default: 'ValidDetection'), detectionInstance.toString()])}"
                redirect(action: "show", id: detectionInstance.id)
            }
            else {
                render(view: "edit", model: [detectionInstance: detectionInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'ValidDetection'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def detectionInstance = ValidDetection.get(params.id)
        if (detectionInstance) {
            try {
                detectionInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'detection.label', default: 'ValidDetection'), detectionInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'detection.label', default: 'ValidDetection'), detectionInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'ValidDetection'), params.id])}"
            redirect(action: "list")
        }
    }

    def _checkSpeciesFilter() {
        def speciesFilterValue = params.filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in.grep{ it.trim() }
        def filteredOnSpecies = speciesFilterValue.size() > 1

        println "filteredOnSpecies: $filteredOnSpecies"

        params.allowSanitisedResults = !filteredOnSpecies
    }
}
