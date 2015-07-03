package au.org.emii.aatams.detection

import au.org.emii.aatams.report.ReportController
import groovy.sql.Sql
import java.text.DateFormat
import java.text.SimpleDateFormat

class DetectionController extends ReportController {
    def candidateEntitiesService
    def dataSource
    def detectionExtractService

    static allowedMethods = [update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = params.max ?: grailsApplication.config.grails.gorm.default.list.nonPaginatedMax
        doList("detection")
    }

    protected def displayCountMessage(resultList, queryName) {
    }

    protected void cleanDateParams() {
        [1, 2].each {
            if (params["filter.between." + it] && params["filter.between." + it].class == String) {
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

    protected def getResultList(queryName) {
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

        def detections = detectionExtractService.extractPage(params).results

        params.remove("sql")
        params.remove("projectPermissionCache")

        [ results: detections ]
    }

    def export = {
        if (['KMZ', 'KMZ (tag tracks)', 'KMZ (bubble plot)'].contains(params._action_export)) {
            //            doExport("detection")
            assert(false): "This is functionality is currently disabled, in order to implement to 'Protected Species' feature."
        }
        else {
            detectionExtractService.generateReport(params, request, response)
        }
    }

    def create = {
        redirect(controller:"receiverDownloadFile",
                 action:"createDetections")
    }

    def show = {
        def detectionInstance = DetectionView.get(params.id, dataSource)
        if (!detectionInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'detection.label', default: 'DetectionView'), params.id])}"
            redirect(action: "list")
        }
        else {
            [ detectionInstance: detectionInstance ]
        }
    }
}
