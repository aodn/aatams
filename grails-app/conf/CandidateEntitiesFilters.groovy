import org.apache.shiro.SecurityUtils

/**
 * This filter inserts "candidate" entities in the model sent to the view.
 * 
 * As an example, when creating a new installation, only projects for which the
 * authenticated user can write to should be shown in the "projects" select list.
 * These are the candidate projects.
 * 
 * @author jburgess
 */
class CandidateEntitiesFilters 
{
    def dependsOn = [EmbargoFilters.class]
    
    def candidateEntitiesService

    def filters = 
    {
        all(controller:'*', action:'create|edit|save|update|addSurgery')
        {
/**            
            after = {model ->

                if (   !SecurityUtils.subject.isAuthenticated()
                    && (controllerName == "person"))
                {
                    // Don't do anything if user is not authenticated (as the below
                    // code is expecting an authenticated user.
                }
                else
                {
                    def candidateProjects = candidateEntitiesService.projects()
                    model?.candidateProjects = candidateProjects

                    def candidateInstallations = candidateEntitiesService.installations()
                    model?.candidateInstallations = candidateInstallations

                    def candidateStations = candidateEntitiesService.stations()
                    model?.candidateStations = candidateStations

                    def candidateDeployments = candidateEntitiesService.deployments()
                    model?.candidateDeployments = candidateDeployments

                    def candidateSurgeries = candidateEntitiesService.surgeries()
                    model?.candidateSurgeries = candidateSurgeries

                    def candidateReceivers = candidateEntitiesService.receivers()

                    model?.candidateReceivers = candidateReceivers

                    //
                    // Specific to animal release edit.
                    //
                    def candidateTags = candidateEntitiesService.tags(model?.animalReleaseInstance?.project)
                    model?.candidateTags = candidateTags

                    def candidatePeople = candidateEntitiesService.people(model?.animalReleaseInstance?.project)
                    model?.candidatePeople = candidatePeople
                }
            }
*/                
        }
        
        embargoPeriod(controller:'animalRelease', action:'create|edit')
        {
            after =
            {
                model ->
                
                // The list of embargo periods to choose from.
                def embargoPeriods = [6: '6 months', 12: '12 months']
                model?.embargoPeriods = embargoPeriods.entrySet()
            }
        }
    }
}

