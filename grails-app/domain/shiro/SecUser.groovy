package shiro

class SecUser {
    String username
    String passwordHash
    
    def defaultTimeZone
    
    static hasMany = [ roles: SecRole, permissions: String ]

    static constraints = {
        username(nullable: false, blank: false, unique:true)
    }
}
