package au.org.emii.aatams.detection

import org.springframework.jdbc.core.JdbcTemplate

import au.org.emii.aatams.Surgery

class JdbcTemplateDetectionFactoryService extends DetectionFactoryService
{
    def dataSource
    def sessionFactory

    protected def createValidDetection(params)
    {
        return (params
                + ["valid": true,
                   "clazz": "au.org.emii.aatams.detection.ValidDetection",
                   "receiverDownloadId": params.receiverDownload.id,
                   "receiverDeploymentId": params.receiverDeployment.id,
                   "message": "",
                   "reason": "",
                   "provisional": true,
                   "detectionSurgeries": new ArrayList()])
    }

    protected def createInvalidDetection(params)
    {
        return (params
                + ["valid": false,
                   "clazz": "au.org.emii.aatams.detection.InvalidDetection",
                   "receiverDownloadId": params.receiverDownload.id,
                   "detectionSurgeries": new ArrayList()])
    }

    void batchUpdate(String[] statements)
    {
        log.debug("Inserting " + statements.size() + " records...")
        JdbcTemplate insert = new JdbcTemplate(dataSource)
        insert.batchUpdate(statements)
        log.debug("Batch successfully inserted")
    }
}
