package au.org.emii.aatams.detection

import au.org.emii.aatams.AnimalRelease
import org.apache.shiro.SecurityUtils

class DetectionVisibilityChecker {
    def row
    def params
    def permissionUtilsService
    def releaseIsProtectedCache

    DetectionVisibilityChecker(theRow, theParams, thePermissionUtilsService, theReleaseIsProtectedCache) {
        row = theRow
        params = theParams
        permissionUtilsService = thePermissionUtilsService
        releaseIsProtectedCache = theReleaseIsProtectedCache
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
        row.release_project_id && releaseIsProtected()
    }

    def releaseIsProtected() {

        def releaseId = row.animal_release_id

        if (!releaseIsProtectedCache.containsKey(releaseId)) {

            def release = AnimalRelease.get(releaseId)

            releaseIsProtectedCache[releaseId] = release.protectionActive
        }

        return releaseIsProtectedCache[releaseId]
    }

    def _isEmbargoed() {
        row.embargo_date && row.embargo_date.after(new Date())
    }

    def _allowSanitisedResults() {
        !_isFilteredOnSpecies(params.filter)
    }

    def _isFilteredOnSpecies(filter) {
        def speciesFilterValue = filter?.surgeries?.release?.animal?.species?.in?.grep{ it.trim() }
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
