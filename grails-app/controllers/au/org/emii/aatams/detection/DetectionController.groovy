package au.org.emii.aatams.detection

import au.org.emii.aatams.report.ReportController
import groovy.sql.Sql

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
        // ValidDetection.metaClass.static.count = {
        //     println("*** controller overridden count")
        //     return au.org.emii.aatams.Statistics.getStatistic('numValidDetections')
        // }
        
		doList("detection")
    }

    protected def getResultList(queryName)
    {
        params.sql = new Sql(dataSource)
        params.projectPermissionCache = [:]

        def detections = detectionExtractService.applyEmbargo(detectionExtractService.extractPage(params), params)
        detections = detections.collect {
            ValidDetection.get(it.detection_id)
        }

        def count

        if (!params || params.isEmpty() || params?.filter == null || params?.filter == [:])
        {
			count = ValidDetection.count()
		}
        else
        {
            count = detectionExtractService.getCount(params)
        }
        
        //        flattenParams()
        //		flash.message = "${count} matching records (${ValidDetection.count()} total)."
        
        params.remove("sql")
        params.remove("projectPermissionCache")
        
        [results: detections, count: count]
	}

	def export =
	{
		if (['KMZ', 'KMZ (tag tracks)', 'KMZ (bubble plot)'].contains(params._action_export))
		{
			doExport("detection")
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
