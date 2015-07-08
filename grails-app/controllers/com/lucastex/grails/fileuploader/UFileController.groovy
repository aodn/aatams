package com.lucastex.grails.fileuploader

class UFileController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [UFileInstanceList: UFile.list(params), UFileInstanceTotal: UFile.count()]
    }

    def create = {
        def UFileInstance = new UFile()
        UFileInstance.properties = params
        return [UFileInstance: UFileInstance]
    }

    def save = {
        def UFileInstance = new UFile(params)
        if (UFileInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'UFile.label', default: 'UFile'), UFileInstance.id])}"
            redirect(action: "show", id: UFileInstance.id)
        }
        else {
            render(view: "create", model: [UFileInstance: UFileInstance])
        }
    }

    def show = {
        def UFileInstance = UFile.get(params.id)
        if (!UFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            [UFileInstance: UFileInstance]
        }
    }

    def edit = {
        def UFileInstance = UFile.get(params.id)
        if (!UFileInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [UFileInstance: UFileInstance]
        }
    }

    def update = {
        def UFileInstance = UFile.get(params.id)
        if (UFileInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (UFileInstance.version > version) {

                    UFileInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'UFile.label', default: 'UFile')] as Object[], "Another user has updated this UFile while you were editing")
                    render(view: "edit", model: [UFileInstance: UFileInstance])
                    return
                }
            }
            UFileInstance.properties = params
            if (!UFileInstance.hasErrors() && UFileInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'UFile.label', default: 'UFile'), UFileInstance.id])}"
                redirect(action: "show", id: UFileInstance.id)
            }
            else {
                render(view: "edit", model: [UFileInstance: UFileInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def UFileInstance = UFile.get(params.id)
        if (UFileInstance) {
            try {
                UFileInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'UFile.label', default: 'UFile'), params.id])}"
            redirect(action: "list")
        }
    }
}
