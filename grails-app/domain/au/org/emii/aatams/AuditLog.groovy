package au.org.emii.aatams

class AuditLog {

	def dateCreated
	
	// One of "create", "edit", "delete".
	String action
	
	// Key to the entity which is acted upon.
	Long entityId
	Class clazz
	
	// This is here for the "delete" case, since it won't be possible to get to a deleted entity, to see what it was.
	String description
	
	static belongsTo = [person: Person]
}
