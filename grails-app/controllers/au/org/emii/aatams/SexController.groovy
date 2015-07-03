package au.org.emii.aatams

class SexController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [sexInstanceList: Sex.list(params), sexInstanceTotal: Sex.count()]
    }

    def create = {
        def sexInstance = new Sex()
        sexInstance.properties = params
        return [sexInstance: sexInstance]
    }

    def save = {
        def sexInstance = new Sex(params)
        if (sexInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'sex.label', default: 'Sex'), sexInstance.toString()])}"
            redirect(action: "show", id: sexInstance.id)
        }
        else {
            render(view: "create", model: [sexInstance: sexInstance])
        }
    }

    def show = {
        def sexInstance = Sex.get(params.id)
        if (!sexInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sex.label', default: 'Sex'), params.id])}"
            redirect(action: "list")
        }
        else {
            [sexInstance: sexInstance]
        }
    }

    def edit = {
        def sexInstance = Sex.get(params.id)
        if (!sexInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sex.label', default: 'Sex'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [sexInstance: sexInstance]
        }
    }

    def update = {
        def sexInstance = Sex.get(params.id)
        if (sexInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (sexInstance.version > version) {

                    sexInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'sex.label', default: 'Sex')] as Object[], "Another user has updated this Sex while you were editing")
                    render(view: "edit", model: [sexInstance: sexInstance])
                    return
                }
            }
            sexInstance.properties = params
            if (!sexInstance.hasErrors() && sexInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'sex.label', default: 'Sex'), sexInstance.toString()])}"
                redirect(action: "show", id: sexInstance.id)
            }
            else {
                render(view: "edit", model: [sexInstance: sexInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sex.label', default: 'Sex'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def sexInstance = Sex.get(params.id)
        if (sexInstance) {
            try {
                sexInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'sex.label', default: 'Sex'), sexInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'sex.label', default: 'Sex'), sexInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'sex.label', default: 'Sex'), params.id])}"
            redirect(action: "list")
        }
    }
}
