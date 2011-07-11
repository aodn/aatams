package au.org.emii.aatams

import org.apache.shiro.crypto.hash.Sha256Hash

class PersonController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", updatePassword: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [personInstanceList: Person.list(params), personInstanceTotal: Person.count()]
    }

    def create = {
        def personInstance = new Person()
        personInstance.properties = params
        return [personInstance: personInstance]
    }

    def save = 
    {   
        PersonCreateCommand createPersonCmd ->
        
        if (createPersonCmd.validate())
        {
            def personInstance = createPersonCmd.createPerson()
            if (personInstance.save(flush: true)) 
            {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'person.label', default: 'Person'), personInstance.id])}"
                redirect(action: "show", id: personInstance.id)
            }
            else 
            {
                render(view: "create", model: [createPersonCmd:createPersonCmd])
            }
        }
        else
        {
            render(view: "create", model: [createPersonCmd:createPersonCmd])
        }
    }

    def show = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else {
            [personInstance: personInstance]
        }
    }

    def edit = {
        def personInstance = Person.get(params.id)
        if (!personInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [personInstance: personInstance]
        }
    }

    def update = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (personInstance.version > version) {
                    
                    personInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'person.label', default: 'Person')] as Object[], "Another user has updated this Person while you were editing")
                    render(view: "edit", model: [personInstance: personInstance])
                    return
                }
            }
            personInstance.properties = params
            if (!personInstance.hasErrors() && personInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'person.label', default: 'Person'), personInstance.id])}"
                redirect(action: "show", id: personInstance.id)
            }
            else {
                render(view: "edit", model: [personInstance: personInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
    }

    def updatePassword = 
    {
        PersonUpdatePasswordCommand updatePwdCmd ->
        
        if (   updatePwdCmd.validate()
            && updatePwdCmd.updatePassword())
        {
            flash.message = "${message(code: 'person.password.update', default: 'Password changed')}"
            redirect(action: "show", id: updatePwdCmd.id)
        }
        else
        {
            def personInstance = Person.get(updatePwdCmd.id)
            if (!personInstance) 
            {
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
                redirect(action: "list")
            }
            else 
            {
                return [personInstance: personInstance]
            }
        }
        
    }
    def delete = {
        def personInstance = Person.get(params.id)
        if (personInstance) {
            try {
                personInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'person.label', default: 'Person'), params.id])}"
            redirect(action: "list")
        }
    }
}

/**
 * The main purpose of this command object (on top of Person domain object)
 * is to provide validation that the two given passwords match, and to hash
 * the given password when creating a new Person instance.
 */
class PersonCreateCommand
{
    String name
    String username
    String password         // Plain-text password.
    String passwordConfirm
    String phoneNumber
    String emailAddress
    
    static constraints =
    {
        name(blank:false)
        username(blank:false)
        password(blank:false, validator:{val, obj ->
            if (val != obj.passwordConfirm)
            {
                return "person.password.mismatch"
            }
        })
        passwordConfirm(blank:false)
        phoneNumber(blank:false)
        emailAddress(email:true)
    }
    
    Person createPerson()
    {
        def person = new Person(username:username,
                                passwordHash:new Sha256Hash(password).toHex(),
                                name:name,
                                phoneNumber:phoneNumber,
                                emailAddress:emailAddress)
                            
        return person
    }
}

class PersonUpdatePasswordCommand
{
    Integer id
    String password
    String passwordConfirm
    
    static constraints =
    {
        id(validator:{val ->
            if (!Person.get(val))
            {
                return "person.id.invalid"
            }
        })

        password(blank:false, validator:{val, obj ->
            if (val != obj.passwordConfirm)
            {
                return "person.password.mismatch"
            }
        })
        passwordConfirm(blank:false)
    }

    Person updatePassword()
    {
        def person = Person.get(id)
        person.setPasswordHash(new Sha256Hash(password).toHex())
        if (!person.save(flush:true))
        {
            return null
        }
        
        return person
    }
}
