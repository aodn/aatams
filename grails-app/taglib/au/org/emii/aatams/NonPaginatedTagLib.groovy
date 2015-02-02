package au.org.emii.aatams

class NonPaginatedTagLib {

    def nonPaginatedWarning = {
        out << """
            <div class='paginateButtons'>
                ${g.message(
                    code: 'nonPaginatedMax.warning',
                    args: [grailsApplication.config.grails.gorm.default.list.nonPaginatedMax]
                )}
            </div>"""
    }
}
