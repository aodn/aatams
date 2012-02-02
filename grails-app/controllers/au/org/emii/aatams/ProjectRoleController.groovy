package au.org.emii.aatams

import grails.converters.JSON

class ProjectRoleController {

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST", "GET"]]

    def permissionUtilsService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : grailsApplication.config.grails.gorm.default.list.max, 100)
        [projectRoleInstanceList: ProjectRole.list(params), projectRoleInstanceTotal: ProjectRole.count()]
    }

    def create = {
        def projectRoleInstance = new ProjectRole()
        projectRoleInstance.properties = params
        return [projectRoleInstance: projectRoleInstance]
    }

    def save = 
    {
        log.debug("Adding projectRole, params: " + params)
        
        def projectRoleInstance = new ProjectRole(params)
        log.debug("Role type: " + projectRoleInstance.roleType)
        
        if (projectRoleInstance.save(flush: true)) 
        {
			log.debug("Project role saved: " + projectRoleInstance)
            permissionUtilsService.setPermissions(projectRoleInstance)
			log.debug("Permissions updated for project role: " + projectRoleInstance)
			
            flash.message = "${message(code: 'default.added.message', args: [message(code: 'person.label', default: 'Person'), \
                                                                             projectRoleInstance.person.toString(), \
                                                                             message(code: 'project.label', default: 'Project'), \
                                                                             projectRoleInstance.project.toString()])}"
                                                                     
            render ([instance:projectRoleInstance, message:flash] as JSON)
        }
        else 
        {
            log.error(projectRoleInstance.errors)
            render ([errors:projectRoleInstance.errors] as JSON)
        }
    }

    def show = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (!projectRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            [projectRoleInstance: projectRoleInstance]
        }
    }

    def edit = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (!projectRoleInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [projectRoleInstance: projectRoleInstance]
        }
    }

    def update = {
        def projectRoleInstance = ProjectRole.get(params.id)
        if (projectRoleInstance) 
		{
            if (params.version) 
			{
                def version = params.version.toLong()
                if (projectRoleInstance.version > version) 
				{
                    
                    projectRoleInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'projectRole.label', default: 'ProjectRole')] as Object[], "Another user has updated this ProjectRole while you were editing")
                    render(view: "edit", model: [projectRoleInstance: projectRoleInstance])
                    return
                }
            }
			
			permissionUtilsService.removePermissions(projectRoleInstance)
            projectRoleInstance.properties = params
			
            if (!projectRoleInstance.hasErrors() && projectRoleInstance.save(flush: true)) 
			{
				permissionUtilsService.setPermissions(projectRoleInstance)
				
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), projectRoleInstance.toString()])}"
            
                def projectId = projectRoleInstance?.project?.id
                redirect(controller:"project", action: "edit", id: projectId, params: [projectId:projectId])
            }
            else 
			{
                render(view: "edit", model: [projectRoleInstance: projectRoleInstance])
            }
        }
        else 
		{
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def projectRoleInstance = ProjectRole.get(params.id)
		
        if (projectRoleInstance) 
		{
			def projectId = projectRoleInstance?.project?.id
			
            try 
            {
				log.debug("Removing permissions related to project role: " + projectRoleInstance)
                permissionUtilsService.removePermissions(projectRoleInstance)
				log.debug("Removing project role: " + projectRoleInstance)
                projectRoleInstance.delete(flush: true)
				log.debug("Project role removed")
                
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), projectRoleInstance.toString()])}"
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), projectRoleInstance.toString()])}"
            }
            
			log.debug("Redirecting to project page, project ID: " + projectId)
            redirect(controller:"project", action: "edit", id: projectId, params: [projectId:projectId])
        }
        else 
		{
			log.error("Project role not found, id: " + params.id)
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'projectRole.label', default: 'ProjectRole'), params.id])}"
            redirect(action: "list")
        }
    }
}
