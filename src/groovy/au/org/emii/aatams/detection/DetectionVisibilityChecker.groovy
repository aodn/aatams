package au.org.emii.aatams.detection

import au.org.emii.aatams.Project
import org.apache.shiro.SecurityUtils

class DetectionVisibilityChecker {
    def row
    def params
    def permissionUtilsService
    def projectIsProtectedCache

    DetectionVisibilityChecker(theRow, theParams, thePermissionUtilsService, theProjectIsProtectedCache) {
        row = theRow
        params = theParams
        permissionUtilsService = thePermissionUtilsService
        projectIsProtectedCache = theProjectIsProtectedCache
    }

    def apply() {

        if (!shouldKeep()) {
            return null
        }

        if (shouldSanitise()) {
            sanitise()
        }

        return row
    }

    def sanitise() {
        row.species_name = ""
        row.spcode = ""
        row.sensor_id = ""
    }

    def shouldKeep() {
        _isPublic() || _isReadable() || (_allowSanitisedResults() && !_isProtected())
    }

    def shouldSanitise() {
        _isEmbargoed() && !_isProtected() && !_isReadable()
    }

    def _isPublic() {
        !(_isEmbargoed() || _isProtected())
    }

    def _isReadable() {
        hasReadPermission(row.release_project_id)
    }

    def _isProtected() {
        row.release_project_id && projectIsProtected(row.release_project_id)
    }

    def projectIsProtected(projectId) {
        if (!projectIsProtectedCache.containsKey(projectId)) {

            def project = Project.get(projectId)

            projectIsProtectedCache[projectId] = project.isProtected
        }

        return projectIsProtectedCache[projectId]
    }


    def _isEmbargoed() {
        row.embargo_date && row.embargo_date.after(new Date())
    }

    def _allowSanitisedResults() {
        !_isFilteredOnSpecies(params.filter)
    }

    def _isFilteredOnSpecies(filter) {
        def speciesFilterValue = filter?.detectionSurgeries?.surgery?.release?.animal?.species?.in?.grep{ it.trim() }
        speciesFilterValue?.size() > 1
    }

    def hasReadPermission(projectId) {
        if (projectId == null) {
            return true
        }

        if (!params.projectPermissionCache.containsKey(projectId)) {
            String permissionString = permissionUtilsService.buildProjectReadPermission(projectId)
            params.projectPermissionCache.put(projectId, SecurityUtils.subject.isPermitted(permissionString))
        }

        assert (params.projectPermissionCache.containsKey(projectId))
        return params.projectPermissionCache[projectId]
    }
}
