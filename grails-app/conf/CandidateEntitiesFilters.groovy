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
            }
        }
    }
}

