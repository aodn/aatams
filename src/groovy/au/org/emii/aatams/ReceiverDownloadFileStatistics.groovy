package au.org.emii.aatams

import groovy.sql.Sql

class ReceiverDownloadFileStatistics {

    def counts
    def unknownReceivers

    def refresh(dataSource, receiverDownload) {
        def sql = new Sql(dataSource)

        counts = [:]
        sql.eachRow(
            """select count(*), invalid_reason
               from detection_view where receiver_download_id = ${receiverDownload.id}
               group by invalid_reason"""
        ) { row ->
            counts[row.invalid_reason] = row.count
        }

        unknownReceivers = sql.rows(
            """select distinct receiver_name
               from detection_view where receiver_download_id = ${receiverDownload.id}
               and invalid_reason = 'UNKNOWN_RECEIVER'"""
        ).collect { row ->
            row.receiver_name
        }.sort()
    }

    def getValidCount() {
        counts[null] ?: 0
    }

    def getDuplicateCount() {
        counts['DUPLICATE'] ?: 0
    }

    def getNoDeploymentAndRecoveryCount() {
        counts['NO_DEPLOYMENT_AND_RECOVERY_AT_DATE_TIME'] ?: 0
    }

    def getUnknownReceiverCount() {
        counts['UNKNOWN_RECEIVER'] ?: 0
    }

    def getInvalidCount() {
        duplicateCount + unknownReceiverCount + noDeploymentAndRecoveryCount
    }

    def getTotalCount() {
        validCount + invalidCount
    }
}
