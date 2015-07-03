package au.org.emii.aatams

class AddressController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [addressInstanceList: Address.list(params), addressInstanceTotal: Address.count()]
    }

    def create = {
        def addressInstance = new Address()
        addressInstance.properties = params
        return [addressInstance: addressInstance]
    }

    def save = {
        def addressInstance = new Address(params)
        if (addressInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'address.label', default: 'Address'), addressInstance.toString()])}"
            redirect(action: "show", id: addressInstance.id)
        }
        else {
            render(view: "create", model: [addressInstance: addressInstance])
        }
    }

    def show = {
        def addressInstance = Address.get(params.id)
        if (!addressInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'address.label', default: 'Address'), params.id])}"
            redirect(action: "list")
        }
        else {
            [addressInstance: addressInstance]
        }
    }

    def edit = {
        def addressInstance = Address.get(params.id)
        if (!addressInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'address.label', default: 'Address'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [addressInstance: addressInstance]
        }
    }

    def update = {
        def addressInstance = Address.get(params.id)
        if (addressInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (addressInstance.version > version) {

                    addressInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'address.label', default: 'Address')] as Object[], "Another user has updated this Address while you were editing")
                    render(view: "edit", model: [addressInstance: addressInstance])
                    return
                }
            }
            addressInstance.properties = params
            if (!addressInstance.hasErrors() && addressInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'address.label', default: 'Address'), addressInstance.toString()])}"
                redirect(action: "show", id: addressInstance.id)
            }
            else {
                render(view: "edit", model: [addressInstance: addressInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'address.label', default: 'Address'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def addressInstance = Address.get(params.id)
        if (addressInstance) {
            try {
                addressInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'address.label', default: 'Address'), addressInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'address.label', default: 'Address'), addressInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'address.label', default: 'Address'), params.id])}"
            redirect(action: "list")
        }
    }
}
