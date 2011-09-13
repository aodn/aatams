package au.org.emii.aatams.report

import au.org.emii.aatams.*
import org.joda.time.*

class AnimalReleaseSummaryService 
{
    static transactional = true

    
    def countBySpecies() 
    {
        def countsBySpecies = [:]
        
        AnimalRelease.list().each
        {
            release ->
            
            Species species = release.animal.species
            
            if (!countsBySpecies.get(species))
            {
                countsBySpecies[species] = new AnimalReleaseCount(species:species, 
                                                                  currentReleases:0, 
                                                                  totalReleases:0)
            }
            
            // Increment total
            countsBySpecies[species].totalReleases = countsBySpecies[species].totalReleases + 1
            
            if (release.isCurrent())
            {
                countsBySpecies[species].currentReleases = countsBySpecies[species].currentReleases + 1
            }
        }
        
        return sortByTotalReleases(countsBySpecies.values())
    }
    
    def sortByTotalReleases(def counts)
    {
        return counts.sort(
        {
            a, b ->
            
            b.totalReleases <=> a.totalReleases
        })
    }
    
    Map<String, Long> summary()
    {
        def retMap = ["% embargoed":0,
                      "last 30 days":0,
                      "this year":0,
                      "total":0,
                      "embargoed":0,
                      "count by species":null]

        AnimalRelease.list().each
        {
            release ->
            
            retMap["total"] = retMap["total"] + 1

            if (last30Days(release.releaseDateTime))
            {
                retMap["last 30 days"] = retMap["last 30 days"] + 1
            }
            
            if (thisYear(release.releaseDateTime))
            {
                retMap["this year"] = retMap["this year"] + 1
            }
            
            if (release.isEmbargoed())
            {
                retMap["embargoed"] = retMap["embargoed"] + 1
            }
        }
        
        retMap["% embargoed"] = retMap["embargoed"] * 100 / retMap["total"]
        retMap["count by species"] = countBySpecies()
        
        return retMap
    }
    
    def executeReportQuery(def params)
    {
        return [summary()]
    }
    
    private boolean last30Days(DateTime date)
    {
        DateTime datePlus30 = date.plusDays(30)
        return datePlus30.isAfter(now())
    }
    
    private boolean thisYear(DateTime date)
    {
        return date.year() == now().year()
    }
    
    private DateTime now()
    {
        return new DateTime()
    }
}
