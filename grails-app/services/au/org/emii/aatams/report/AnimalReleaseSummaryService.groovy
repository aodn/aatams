package au.org.emii.aatams.report

import au.org.emii.aatams.*
import org.joda.time.*

class AnimalReleaseSummaryService 
{
    static transactional = false

    
    List<AnimalReleaseCount> countBySpecies() 
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
                                                                  totalReleases:0,
                                                                  totalEmbargoed:0,
                                                                  percentEmbargoed:0.0f)
            }
            
            // Increment total
            countsBySpecies[species].totalReleases = countsBySpecies[species].totalReleases + 1
            
            if (release.isCurrent())
            {
                countsBySpecies[species].currentReleases = countsBySpecies[species].currentReleases + 1
            }
            
            if (release.isEmbargoed())
            {
                countsBySpecies[species].totalEmbargoed = countsBySpecies[species].totalEmbargoed + 1
            }
            
            countsBySpecies[species].percentEmbargoed = (float)countsBySpecies[species].totalEmbargoed / countsBySpecies[species].totalReleases * 100
        }
        
        return sortByTotalReleases(countsBySpecies.values())
    }
    
    Map<String, Number> summary()
    {
        def retMap = ["% embargoed":Float.valueOf(0),
                      "last 30 days":Long.valueOf(0),
                      "this year":Long.valueOf(0),
                      "total":Long.valueOf(0),
                      "embargoed":Long.valueOf(0)]

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
        
        if (retMap["total"] != 0)
        {
            retMap["% embargoed"] = Float.valueOf(retMap["embargoed"] * 100 / retMap["total"])
        }
        
        retMap["count by species"] = countBySpecies()
        
        return retMap
    }
    
    private List sortByTotalReleases(def counts)
    {
        return counts.sort(
        {
            a, b ->
            
            b.totalReleases <=> a.totalReleases
        })
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
