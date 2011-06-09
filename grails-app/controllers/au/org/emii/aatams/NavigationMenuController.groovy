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
        'Organisation',
        'Person',
        'Project'
    ]
    
    def installationDataControllers =
    [
        'Installation',
        'InstallationStation',
        'Receiver',
        'Tag'
    ]
    
    def fieldDataControllers =
    [
        'AnimalRelease',
        'Detection',
        'ReceiverDeployment',
        'ReceiverRecovery',
    ]
    
    def reportControllers =
    [
        // TODO
    ]

    // Only visible to administrators...
    def adminControllers =
    [
        'Animal',
        'AnimalMeasurement',
        'AnimalMeasurementType',
        'Detection',
        'Device',
        'DeviceModel',
        'DeviceStatus',
        'InstallationConfiguration',
        'MeasurementUnit',
        'MooringType',
        'OrganisationPerson',
        'OrganisationProject',
        'ProjectRole',
        'ProjectRoleType',
        'ReceiverDownload',
        'ReceiverDownloadFile',
        'Sensor',
        'SensorDetection',
        'Sex',
        'Surgery',
        'SurgeryTreatment',
        'SurgeryType',
        'SystemRole',
        'SystemRoleType'
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
