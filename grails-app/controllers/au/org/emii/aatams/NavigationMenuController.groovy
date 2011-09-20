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
    
//    def fieldDataControllers =
//    [
//        'tag':"Tags",
//        'animalRelease':"Tag Releases",
//        'detection':"Tag Detections",
//        'receiverDeployment':"Receiver Deployment",
//        'receiverRecovery':"Receiver Recovery",
//        'receiverEvent':"Receiver Events"
//    ]
    
    def fieldDataControllers()
    {
        [
            new NavigationMenuItem(controllerName:'tag', label:'Tags', canCreateNew:SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildProjectWriteAnyPermission())),
            new NavigationMenuItem(controllerName:'animalRelease', label:'Tag Releases', canCreateNew:SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildProjectWriteAnyPermission())),
            new NavigationMenuItem(controllerName:'detection', label:'Tag Detections', canCreateNew:SecurityUtils.getSubject().hasRole("SysAdmin")),
            new NavigationMenuItem(controllerName:'receiverDeployment', label:'Receiver Deployment', canCreateNew:SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildProjectWriteAnyPermission())),
            new NavigationMenuItem(controllerName:'receiverRecovery', label:'Receiver Recovery', canCreateNew:SecurityUtils.getSubject().isPermitted(permissionUtilsService.buildProjectWriteAnyPermission())),
            new NavigationMenuItem(controllerName:'receiverEvent', label:'Receiver Events', canCreateNew:SecurityUtils.getSubject().hasRole("SysAdmin"))
        ]
    }
        
    def reportControllers =
    [
        // TODO
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
         reportControllers: reportControllers,
         helpControllers: helpControllers,
         adminControllers: adminControllers,
         blacklistControllers: blacklistControllers
        ]
    }
}
