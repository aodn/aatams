package au.org.emii.aatams.report

import au.org.emii.aatams.*

class ReportController 
{
    def reportInfoService
    def reportQueryExecutorService
    
    def index = { }
    
    def create =
    {
        return [name:params.name]
    }
    
    def execute =
    {
        log.debug("Executing report, params: " + params)
        
        def resultList = reportQueryExecutorService.executeQuery(Receiver, params.filter)
        
        // Delegate to report controller, including our wrapped data.
        chain(controller:'jasper', 
              action:'indexWithSession',
              model:[data:resultList],
              params:params)
    }
    
    def receiverCreate =
    {
        redirect(action:"create", params:[name:"receiver"])
    }
}
