package au.org.emii.aatams

/**
 * Controller for the navigation menu which appears on the "west" panel of
 * all views.
 * 
 * Controllers are grouped under the following categories:
 * 
 *  - Background Data
 *  - Installation Data
 *  - Field Data
 *  - Data Download
 *  
 * 
 */
class NavigationMenuController 
{
    
    def backgroundDataControllers = 
    [
        organisation:"Organisation",
        project:"Projects",
        person:"People"
    ]
    
    def installationDataControllers =
    [
        'installation':"Installations",
        'installationStation':"Stations",
        'receiver':"Receivers"
    ]
    
    def fieldDataControllers =
    [
        'tag':"Tags",
        'animalRelease':"Tag Releases",
        'detection':"Tag Detections",
        'receiverDeployment':"Receiver Deployment",
        'receiverRecovery':"Receiver Recovery",
        'receiverEvent':"Receiver Events"
    ]
    
    /**
     * All reports use the same controller, just a different action.
     */
    def reportActions = 
    [
        'installationStationCreate':"Installations",
        'receiverCreate':"Receivers",
        'receiverDeploymentCreate':"Receiver Deployments",
        'animalReleaseSummaryCreate':"Tag Summary"
    ]

    def dataExtractActions = 
    [
        'installationStationExtract':"Installations",
        'tagExtract':"Tags"
    ]
    
    def helpControllers =
    [
        'gettingStarted':"Getting Started",
        'about':"About"
    ]

    // Only visible to administrators...
    def adminControllers =
    [
        'animal',
        'animalMeasurement',
        'animalMeasurementType',
        'detection',
        'device',
        'deviceModel',
        'deviceStatus',
        'installationConfiguration',
        'measurementUnit',
        'mooringType',
        'organisationPerson',
        'organisationProject',
        'projectRole',
        'projectRoleType',
        'receiverDownload',
        'receiverDownloadFile',
        'sensor',
        'sensorDetection',
        'sex',
        'surgery',
        'surgeryTreatment',
        'surgeryType',
        'systemRole',
        'systemRoleType'
    ]
        
    def blacklistControllers =
    [
        'NavigationMenu'
    ]
    
    def index =
    {
        [backgroundDataControllers: backgroundDataControllers,
         installationDataControllers: installationDataControllers,
         fieldDataControllers: fieldDataControllers,
         reportActions: reportActions,
         dataExtractActions: dataExtractActions,
         helpControllers: helpControllers,
         adminControllers: adminControllers,
         blacklistControllers: blacklistControllers
        ]
    }
}
