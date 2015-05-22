package au.org.emii.aatams.detection

import au.org.emii.aatams.AnimalRelease
import org.apache.shiro.SecurityUtils
import org.joda.time.DateTime

class DetectionVisibilityChecker {
    def detection
    def params
    def permissionUtilsService
    def releaseIsProtectedCache

    DetectionVisibilityChecker(theDetection, theParams, thePermissionUtilsService, theReleaseIsProtectedCache) {
        detection = theDetection
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

        return detection
    }

    def sanitise() {
        detection.spcode = ''
        detection.scientificName = ''
        detection.commonName = ''
        detection.sensorId = ''
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
        hasReadPermission(detection.releaseProjectId)
    }

    def _isProtected() {
        detection.releaseProjectId && releaseIsProtected()
    }

    def releaseIsProtected() {

        def releaseId = detection.releaseId

        if (!releaseIsProtectedCache.containsKey(releaseId)) {

            def release = AnimalRelease.get(releaseId)

            releaseIsProtectedCache[releaseId] = release.protectionActive
        }

        return releaseIsProtectedCache[releaseId]
    }

    def _isEmbargoed() {
        detection.embargoDate && detection.embargoDate.isAfter(new DateTime())
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
