package au.org.emii.aatams

/**
 * Code map, specific to Vemco code maps currently.
 
 * @author jburgess
 */
class CodeMap 
{
	String codeMap
	static hasMany = [tags:Tag]
	
    static constraints = 
	{
		codeMap(nullable:false, blank:false, unique:true)
    }
	
	static mapping =
	{
		sort "codeMap"
	}
	
	String toString()
	{
		return codeMap
	}
}
