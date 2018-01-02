package au.org.emii.aatams

/**
 * Determines access priviliges that a Person has on a particular Project (via
 * ProjectRole).
 */
enum ProjectAccess  {
    NO_ACCESS('Revoked'),
    READ_ONLY('Read Only'),
    READ_WRITE('Read/Write')

    String displayStatus

    ProjectAccess(String displayStatus) {
        this.displayStatus = displayStatus
    }

    String toString() {
        return displayStatus
    }

    static list() {
        [NO_ACCESS, READ_ONLY, READ_WRITE]
    }
}
