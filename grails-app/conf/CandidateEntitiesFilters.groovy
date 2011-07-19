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
        all(controller:'*', action:'create|edit')
        {
            after = {model ->
                
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
    }
}

