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
    
    def reportControllers =
    [
        // TODO
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
         reportControllers: reportControllers,
         adminControllers: adminControllers,
         blacklistControllers: blacklistControllers
        ]
    }
}
