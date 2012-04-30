package au.org.emii.aatams

import org.apache.shiro.SecurityUtils

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
    def permissionUtilsService
    
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
    
    def fieldDataControllers()
    {
        boolean projectWriteAny = 
            SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildProjectWriteAnyPermission())
                
        [
            new NavigationMenuItem(controllerName:'sensor', label:'Tags', canCreateNew:projectWriteAny),
            new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:projectWriteAny),
            new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:projectWriteAny),
            new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:projectWriteAny),
            new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:false),
            new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:projectWriteAny),
            new NavigationMenuItem(controllerName:'receiverDownloadFile', label:'Receiver Exports', canCreateNew:false)
        ]
    }
        
    /**
     * All reports use the same controller, just a different action.
     */
    def reportActions = 
    [
        'animalReleaseSummaryCreate':"Tag Summary"
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
         fieldDataControllers: fieldDataControllers(),
         reportActions: reportActions,
         helpControllers: helpControllers,
         adminControllers: adminControllers,
         blacklistControllers: blacklistControllers
        ]
    }
}
