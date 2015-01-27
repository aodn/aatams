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
        doList("detection")
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

        def detections = detectionExtractService.extractPage(params).collect { ValidDetection.toPresentationFormat(it) }

        def paramsClone = params.clone()

        paramsClone.max = grailsApplication.config.filter.count.max + 1
        def count = detectionExtractService.getCount(paramsClone)

        params.remove("sql")
        params.remove("projectPermissionCache")

        [results: detections, count: count]
    }

    def export =
    {
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
}
