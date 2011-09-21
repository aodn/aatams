package au.org.emii.aatams

import au.org.emii.aatams.command.*

import org.apache.shiro.SecurityUtils

class ProjectController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def permissionUtilsService
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        
        def projectTotal = Project.count()
        def projectList = Project.list(params)

        if (!SecurityUtils.getSubject().hasRole("SysAdmin"))
        {
            // Filter out non-ACTIVE organisations (only sys admin should see these).
            projectList = projectList.grep
            {
                return (it.status == EntityStatus.ACTIVE)
            }
            
            // Only count ACTIVE projects.
            // TODO: why doesn't count({}) work?
            projectTotal = 0
            Project.list().each
            {
                if (it.status == EntityStatus.ACTIVE)
                {
                    projectTotal++
                }
            }
        }
        
        [projectInstanceList: projectList, projectInstanceTotal: projectTotal]
    }

    def create = {
        
        def createProjectCmd = new ProjectCreateCommand()
//        createProjectCmd.properties = params
        createProjectCmd.person = Person.findByUsername(SecurityUtils.getSubject().getPrincipal())
        createProjectCmd.organisation = createProjectCmd.person.organisation
        return [createProjectCmd:createProjectCmd]
        
//        def projectInstance = new Project()
//        projectInstance.properties = params
//        return [projectInstance: projectInstance]
    }

    def save = {
        
        ProjectCreateCommand createProjectCmd ->
        
        if (createProjectCmd.validate())
        {
            def projectInstance =  createProjectCmd.createProject()
        
            // If SysAdmin, then set Organisation's status to ACTIVE, otherwise,
            // set to PENDING and record the requesting user.
            if (SecurityUtils.getSubject().hasRole("SysAdmin"))
            {
                projectInstance.status = EntityStatus.ACTIVE
            }
            else
            {
                projectInstance.status = EntityStatus.PENDING
                Person user = Person.findByUsername(SecurityUtils.getSubject().getPrincipal())
                projectInstance.requestingUser = user
            }

            if (projectInstance.save(flush: true)) 
            {
                // Doing this here rather than in the createProject() method
                // of the command, as that was causing StackOVerflowError
                ProjectRoleType pi = ProjectRoleType.findByDisplayName(ProjectRoleType.PRINCIPAL_INVESTIGATOR)
                ProjectRole projectRole = 
                    new ProjectRole(person:createProjectCmd.person, 
                                    project:projectInstance, 
                                    roleType:pi,
                                    access:ProjectAccess.READ_WRITE)
                if (projectRole.save(flush:true))
                {
                    permissionUtilsService.setPermissions(projectRole)
                }
                
                if (SecurityUtils.getSubject().hasRole("SysAdmin"))
                {
                    flash.message = "${message(code: 'default.created.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])}"
                }
                else
                {
                    sendCreationNotificationEmails(projectInstance)
                    flash.message = "${message(code: 'default.requested.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])}"
                }
                redirect(action: "show", id: projectInstance.id)
            }
            else 
            {
                log.error("Error saving projectInstance: " + projectInstance.errors)
//                    render(view: "create", model: [projectInstance: projectInstance])
                render(view: "create", model: [projectInstance:projectInstance, createProjectCmd:createProjectCmd])
            }
        }
        else
        {
            log.error("Create project command invalid.")
            render(view: "create", model: [createProjectCmd:createProjectCmd])
        }
    }

    
    def show = {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
        else 
        {
            [projectInstance: projectInstance, unrelatedOrganisations: projectInstance.unrelatedOrganisations()]
        }
    }

    def edit = {
        def projectInstance = Project.get(params.id)
        if (!projectInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [projectInstance: projectInstance, 
                    unrelatedOrganisations: projectInstance.unrelatedOrganisations()]
        }
    }

    def update = {
        def projectInstance = Project.get(params.id)

        boolean prevPending = (projectInstance.status == EntityStatus.PENDING)

        if (projectInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (projectInstance.version > version) {
                    
                    projectInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'project.label', default: 'Project')] as Object[], "Another user has updated this Project while you were editing")
                    render(view: "edit", model: [projectInstance: projectInstance])
                    return
                }
            }
            projectInstance.properties = params
            if (!projectInstance.hasErrors() && projectInstance.save(flush: true)) 
            {
                // Notify project activated.
                if (prevPending && (projectInstance.status == EntityStatus.ACTIVE))
                {
                    sendActivatedNotificationEmails(projectInstance)
                }
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])}"
                redirect(action: "show", id: projectInstance.id)
            }
            else {
                render(view: "edit", model: [projectInstance: projectInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def projectInstance = Project.get(params.id)
        if (projectInstance) {
            try {
                projectInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'project.label', default: 'Project'), projectInstance.toString()])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.id])}"
            redirect(action: "list")
        }
    }

    /**
     * Notification emails are sent when an project is created (by a non-
     * sys admin) to:
     *
     *  - the requesting user
     *  - AATAMS sys admins.
     */
    def sendCreationNotificationEmails(project)
    {
        sendMail 
        {  
            to project?.requestingUser?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "${message(code: 'mail.request.project.create.subject', args: [project.name])}"     
            body "${message(code: 'mail.request.project.create.body', args: [project.name, createLink(action:'show', id:project.id, absolute:true)])}" 
        }
    }
    
    def sendActivatedNotificationEmails(project)
    {
        sendMail 
        {     
            to project?.requestingUser?.emailAddress
            bcc grailsApplication.config.grails.mail.adminEmailAddress
            from grailsApplication.config.grails.mail.systemEmailAddress
            subject "${message(code: 'mail.request.project.activate.subject', args: [project.name])}"     
            body "${message(code: 'mail.request.project.activate.body', args: [project.name, createLink(action:'show', id:project.id, absolute:true)])}" 
        }
    }
}
