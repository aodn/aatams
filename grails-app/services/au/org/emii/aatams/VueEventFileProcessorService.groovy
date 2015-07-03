package au.org.emii.aatams

import org.grails.plugins.csv.CSVMapReader;


/**
 * Process an event file downloaded from receiver's VUE application.
 */
class VueEventFileProcessorService extends AbstractBatchProcessor {
    def eventFactoryService

    static transactional = true

    void processSingleRecord(downloadFile, map, context) throws FileProcessingException {
        eventFactoryService.newEvent(downloadFile, map)
    }

    protected CSVMapReader getMapReader(downloadFile) {
        def csvMapReader = new CSVMapReader(getReader(downloadFile), [skipLines: 1])

        // Work around for #1016 - sometimes the "units" field consists of a non-quoted value containing commas,
        // which causes the default CSV reader to throw ArrayIndexOutOfBoundsException.
        csvMapReader.fieldKeys = ["Date/Time", "Receiver", "Description", "Data", "Units", "Units detail 1", "Units detail 2", "Units detail 3"]

        return csvMapReader
    }
}
