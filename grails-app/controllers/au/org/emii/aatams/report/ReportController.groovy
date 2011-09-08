package au.org.emii.aatams.report

import au.org.emii.aatams.*
import org.apache.shiro.SecurityUtils

class ReportController 
{
    def reportInfoService
    def reportQueryExecutorService
    
    def permissionUtilsService
    
    def index = { }
    
    def create =
    {
        return [name:params.name]
    }
    
    def execute =
    {
        log.debug("Executing report, params: " + params)

        def resultList = 
            reportQueryExecutorService.executeQuery(reportInfoService.getClassForName(params._name), 
                                                    params.filter)
        
        params.SUBREPORT_DIR = servletContext.getRealPath('/reports') + "/" 
        
        // Put the filter params in flash scope, since the controller chaining
        // below converts everything in "params" to its toString() representation
        // (whereas we want an actual Map delivered to Jasper).
        flash.FILTER_PARAMS = [:]
        
        Person person = permissionUtilsService.principal()
        if (person)
        {
            flash.FILTER_PARAMS.user = person.name
        }

        flash.FILTER_PARAMS = flash.FILTER_PARAMS + reportInfoService.filterParamsToReportFormat(params.filter)
        
        log.debug("Filter params: " + flash.FILTER_PARAMS)
        
        // Delegate to report controller, including our wrapped data.
        chain(controller:'jasper', 
              action:'indexWithSession',
              model:[data:resultList],
              params:params)
    }
    
    def installationStationCreate =
    {
        redirect(action:"create", params:[name:"installationStation"])
    }
    
    def receiverCreate =
    {
        redirect(action:"create", params:[name:"receiver"])
    }
    
    def receiverDeploymentCreate =
    {
        redirect(action:"create", params:[name:"receiverDeployment"])
    }
}
