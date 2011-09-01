package au.org.emii.aatams.report

import au.org.emii.aatams.*

class ReportController 
{
    def index = { }
    
    def installationList =
    {
        return []
    }
    
    def installationListExecute =
    {
        log.debug("Executing installation list report...")
        
        def stationList = InstallationStation.list().collect
        {
            new InstallationStationReportWrapper(it)
        }
        
        log.debug("Chaining to jasper controller, station list: " + stationList)
        
        // Delegate to report controller, including our wrapped data.
        chain(controller:'jasper', 
              action:'index',
              model:[data:stationList],
              params:params)
    }
}
