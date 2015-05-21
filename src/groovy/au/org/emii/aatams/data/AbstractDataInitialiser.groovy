package au.org.emii.aatams.data

import au.org.emii.aatams.PermissionUtilsService

abstract class AbstractDataInitialiser implements DataInitialiser {
    def dataSource
    def permissionUtilsService

    abstract void execute()
}
