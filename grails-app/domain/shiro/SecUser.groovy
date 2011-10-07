package shiro

class SecUser {
    String username
    String passwordHash
    
    Set<String> permissions = new HashSet<String>()
    static hasMany = [ roles: SecRole, permissions: String ]

    static constraints = {
        username(nullable: false, blank: false, unique:true)
    }
}
