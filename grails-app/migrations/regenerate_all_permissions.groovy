import au.org.emii.aatams.PermissionUtilsService
import au.org.emii.aatams.ProjectRole

databaseChangeLog = {
    changeSet(author: "dnahodil", id: "1424221783000-01") {
        grailsChange {
            change {
                def permissionUtilsService = new PermissionUtilsService()

                ProjectRole.list().each {
                    permissionUtilsService.setPermissions(it)

                    it.save(flush: true)
                }
            }
        }
    }
}
