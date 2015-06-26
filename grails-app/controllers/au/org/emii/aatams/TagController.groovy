package au.org.emii.aatams

import au.org.emii.aatams.report.ReportController
import grails.converters.JSON

class TagController extends ReportController
{
    def candidateEntitiesService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list =
    {
        redirect(controller: "sensor", action: "list")
    }

    def create = {
        def tagInstance = new Tag()
        tagInstance.properties = params

        // Default to NEW.
        tagInstance.status = DeviceStatus.NEW

        def model =
            [tagInstance: tagInstance] +
            [candidateProjects:candidateEntitiesService.projects()]
        return model
    }

    def save = {
        def tagInstance = new Tag(params)

        if (tagInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.toString()])}"
            redirect(action: "show", id: tagInstance.id)
        }
        else {
            def model =
                [tagInstance: tagInstance] +
                [candidateProjects:candidateEntitiesService.projects()]
            render(view: "create", model: model)
        }
    }

    def show = {
        def tagInstance = Tag.get(params.id)
        if (!tagInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), params.id])}"
            redirect(action: "list")
        }
        else {
            [tagInstance: tagInstance]
        }
    }

    def edit = {
        def tagInstance = Tag.get(params.id)
        if (!tagInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), params.id])}"
            redirect(action: "list")
        }
        else {
            def model = [tagInstance: tagInstance]
            model.candidateProjects = candidateEntitiesService.projects()
            return model
        }
    }

    def update = {
        def tagInstance = Tag.get(params.id)
        if (tagInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (tagInstance.version > version) {

                    tagInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'tag.label', default: 'Tag')] as Object[], "Another user has updated this Tag while you were editing")
                    render(view: "edit", model: [tagInstance: tagInstance])
                    return
                }
            }
            tagInstance.properties = params
            if (!tagInstance.hasErrors() && tagInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.toString()])}"
                redirect(action: "show", id: tagInstance.id)
            }
            else {
                render(view: "edit", model: [tagInstance: tagInstance, candidateProjects: candidateEntitiesService.projects()])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def tagInstance = Tag.get(params.id)
        if (tagInstance) {
            try {
                tagInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'tag.label', default: 'Tag'), tagInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), params.id])}"
            redirect(action: "list")
        }
    }

    /**
     * Allows auto-complete functionality on front-end.
     */
    def lookupNonDeployedBySerialNumber =
    {
        log.debug("Looking up non-deployed tags, serialNumber: " + params.term)

        def tags = Tag.findAllBySerialNumberIlikeAndStatusNotEqual(params.term + "%", DeviceStatus.DEPLOYED)

        // Limit so that all results fit on screen.
        if (tags?.size() > 20)
        {
            tags = tags[0..19]
        }

        log.debug("Returning: " + (tags as JSON))
        render tags as JSON
    }

    def lookupBySerialNumber =
    {
        def tags = Tag.findAllBySerialNumberIlike(params.term + "%", [sort: "serialNumber"])

        // Limit so that all results fit on screen.
        if (tags?.size() > 20)
        {
            tags = tags[0..19]
        }

        log.debug("Returning: " + (tags as JSON))
        render tags as JSON
    }

    def lookupByCodeName =
    {
        def tags = Tag.findAllByCodeNameIlike("%" + params.term + "%", [max:20, sort:"codeName", order:"asc"])
        def jsonResults = tags.collect
        {
            [label:it.toString(), value:it.codeName]
        }
        render(jsonResults as JSON)
    }
}
