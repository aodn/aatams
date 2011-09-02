package au.org.emii.aatams.report

import au.org.emii.aatams.*

class ReportController 
{
    def reportService
    
    def index = { }
    
    def create =
    {
        return [name:params.name]
    }
    
    def execute =
    {
        log.debug("Executing report, params: " + params)
        
        def resultList = reportService.executeQuery(Receiver, ["codeName": "VR2W-101336"])

        log.debug("Chaining to jasper controller, result list: " + resultList)
        
        // Delegate to report controller, including our wrapped data.
        chain(controller:'jasper', 
              action:'index',
              model:[data:resultList],
              params:params)
    }
    
    def receiverCreate =
    {
        redirect(action:"create", params:[name:"receiver"])
    }
    
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
