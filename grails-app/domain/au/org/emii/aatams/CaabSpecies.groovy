package au.org.emii.aatams

/**
 * Represents a single record/row from CSIRO's CAAB dump:
 *
 *  http://www.cmar.csiro.au/caab/caab_dump_latest.xls
 */
class CaabSpecies extends Species
{
    /**
     * Naming conventions have been kept same as CAAB export file for ease of
     * import in to database.
     *
     * (except for the first three, because uppercase attributes are causing
     * grails dynamic finder problems).
     */
    String spcode
    String commonName
    String scientificName
    String AUTHORITY
    String FAMILY
    String FAMILY_SEQUENCE
    String ASSIGNED_FAMILY_CODE
    String ASSIGNED_FAMILY_SEQUENCE
    String RECENT_SYNONYMS
    String COMMON_NAMES_LIST
    String GENUS
    String SPECIES
    String SCINAME_INFORMAL
    String DATE_LAST_MODIFIED
    String SUBSPECIES
    String VARIETY
    String UNDESCRIBED_SP_FLAG
    String HABITAT_CODE
    String OBIS_CLASSIFICATION_CODE
    String SUBGENUS
    String KINGDOM
    String PHYLUM
    String SUBPHYLUM
    String SPCLASS      // This is known as "CLASS" in the CAAB export, but that causes a naming conflict.
    String SUBCLASS
    String ORDER_NAME
    String SUBORDER
    String INFRAORDER

    static constraints =
    {
        spcode(unique:true)
        commonName(nullable:true)
        scientificName(nullable:true)
        AUTHORITY(nullable:true)
        FAMILY(nullable:true)
        FAMILY_SEQUENCE(nullable:true)
        ASSIGNED_FAMILY_CODE(nullable:true)
        ASSIGNED_FAMILY_SEQUENCE(nullable:true)
        RECENT_SYNONYMS(nullable:true)
        COMMON_NAMES_LIST(nullable:true)
        GENUS(nullable:true)
        SPECIES(nullable:true)
        SCINAME_INFORMAL(nullable:true)
        DATE_LAST_MODIFIED(nullable:true)
        SUBSPECIES(nullable:true)
        VARIETY(nullable:true)
        UNDESCRIBED_SP_FLAG(nullable:true)
        HABITAT_CODE(nullable:true)
        OBIS_CLASSIFICATION_CODE(nullable:true)
        SUBGENUS(nullable:true)
        KINGDOM(nullable:true)
        PHYLUM(nullable:true)
        SUBPHYLUM(nullable:true)
        SPCLASS(nullable:true)
        SUBCLASS(nullable:true)
        ORDER_NAME(nullable:true)
        SUBORDER(nullable:true)
        INFRAORDER(nullable:true)
    }

    static mapping =
    {
        cache true
    }

    String toString()
    {
        return name
    }

	String getName()
	{
		return spcode + " - " + scientificName + " (" + commonName + ")"
	}
}
