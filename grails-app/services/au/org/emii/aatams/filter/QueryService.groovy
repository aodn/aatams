package au.org.emii.aatams.filter

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.criterion.Order
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions

class QueryService 
{
    static transactional = false

	def rootCriteria
	Map subCriteriaMap
	
	/**
	 * Return map:
	 * 
	 * count, rows
	 * 
	 * @return
	 */
    Map<Long, Collection> query(clazz, params) 
	{
		subCriteriaMap = [:]
		
		def results
		def count
		
		if (!params || params.isEmpty())
		{
			results = clazz.list()
			count = clazz.count()
		}
		else
		{
			def criteria = clazz.createCriteria()
			criteria.getInstance()?.setCacheable(true)
		
			def transformedParams = transformParams(params)
			results = criteria.list(transformedParams, buildCriteriaClosure(transformedParams.filter))
			
			count = results.totalCount
		}
	
		return [results: results, count: count]
    }
			
	
	// works
	//		return {
	//			builder.invokeMethod("station", {
	//							installation
	//							{
	//								project
	//								{
	//									eq("name", "Seal Count")
	//									order("name")
	//								}
	//							}
	//						})
	//		}
	
	
	private Closure buildCriteriaClosure(Map params)
	{
		return (
		{
			params?.each  
			{
				method, nestedParams ->
				
println "buildCriteria, method: " + method + ", params: " + nestedParams

				if (method.contains("."))
				{
					// Do nothing.
				}
				else if (nestedParams instanceof List)
				{
					if (nestedParams.count("") > 0)
					{
						// Do nothing.
					}
					else
					{
						invokeMethod(method, nestedParams as Object[])
					}
				}
				else if (nestedParams instanceof Map)
				{
					return invokeMethod(method, buildCriteriaClosure(nestedParams))
				}
				else if (nestedParams.class.isArray())
				{
					invokeMethod(method, nestedParams)
				}
				else
				{
					assert(false) : "Invalid params: " + nestedParams + ", class: " + nestedParams.class
				}
			}
		})
	}
	
	private void build(criteria, propertyName, restrictions)
	{
		if (["sort", "order", "max", "offset"].contains(propertyName))
		{
			// Handled in grails call to "list()".
		}
		else if (propertyName.contains("."))
		{
			// Ignore dotted key names (which come across from gsp to controller) - this data is redundant
			// and is given properly by nested maps.
		}
		else
		{
			restrictions.each
			{
				restrictionType, value ->
				
				if (restrictionType.contains("."))
				{
					
				}
				else if (value == "")
				{
					// Likely a blank form parameter
				}
				else if (restrictionType == "eq")
				{
					log.debug("Adding eq restriction, propertyName: " + propertyName + ", value: " + value)				
					def criterion = Restrictions.eq(propertyName, value)
					criteria.add(criterion)
				}
				else if (restrictionType == "in")
				{
					log.debug("Adding in restriction, propertyName: " + propertyName + ", value: " + value)				
					def criterion = Restrictions.in(propertyName, value)
					criteria.add(criterion)
				}
				else if (restrictionType == "ilike")
				{
					log.debug("Adding ilike restriction, propertyName: " + propertyName + ", value: " + value)				
					def criterion = Restrictions.ilike(propertyName, value)
					criteria.add(criterion)
				}
				else if (restrictionType == "between")
				{
					log.debug("Adding between restriction, propertyName: " + propertyName + ", value: " + value)				
					def criterion = Restrictions.between(propertyName, value.min, value.max)
					criteria.add(criterion)
				}
				else if (restrictionType == "isNull")
				{
					// Doesn't work, see: http://community.jboss.org/wiki/HibernateFAQ-AdvancedProblems#The_query_language_IS_NULL_syntax_wont_work_with_a_onetoone_association
					// criteria.add(Restrictions.isNull(property))
					def alias = criteria.createAlias("recovery", "listRecovery", CriteriaSpecification.LEFT_JOIN)
					alias.add(Restrictions.sqlRestriction("recoverer_id IS NULL"))
				}
				else if (restrictionType == "sort")
				{
					log.debug("Adding sort order, propertyName: " + propertyName + ", value: " + value)	
					
//					if (value == "asc")
//					{			
//						criteria.addOrder(Order.asc(propertyName))
//					}
//					else if (value == "desc")
//					{
//						criteria.addOrder(Order.desc(propertyName))
//					}
//					if (value == "asc")
//					{			
//						criteria.addOrder(Order.asc("project.name"))
//					}
//					else if (value == "desc")
//					{
//						criteria.addOrder(Order.desc(propertyName))
//					}
				}
				else
				{
					log.debug("Adding sub criteria, propertyName: " + propertyName + ", value: " + value)
					
					// Check if already in subcriteria map...
					def subCriteria = subCriteriaMap.get(propertyName)
					
					if (!subCriteria)
					{
						// association
						subCriteria = criteria.createCriteria(propertyName)
						subCriteriaMap[propertyName] = subCriteria
					}
					
					assert(subCriteria)
					assert(value instanceof Map)
					build(subCriteria, restrictionType, value)
				}
			}
		}
	}
	
	private Map transformParams(params)
	{
		def transformedParams = new HashMap(params)
		def sortKey = transformedParams.remove("sort")
		def order = transformedParams.remove("order")
		
		if (sortKey && order)
		{
			def targetMap = transformedParams.filter
			def tokens = sortKey.tokenize(".")
			def sortParam = tokens.pop()
			
			tokens.each
			{
				def nestedTargetMap = targetMap[it]
				
				// This is possible when there is a sort but no restriction.
				if (!nestedTargetMap)
				{
					nestedTargetMap = [:]
					targetMap[it] = nestedTargetMap
				}
				
				targetMap = nestedTargetMap
			}
	
			assert(targetMap != null)
			targetMap.put("order", [sortParam, order])
		}

		return transformedParams
	}
}
