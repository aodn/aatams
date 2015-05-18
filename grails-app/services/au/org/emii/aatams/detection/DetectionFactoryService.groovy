package au.org.emii.aatams.detection

import au.org.emii.aatams.FileFormat
import au.org.emii.aatams.FileProcessingException

class DetectionFactoryService {

    /**
     * Creates a detection given a map of parameters (which originate from a line
     * in a CSV upload file).
     */
    def newDetection(downloadFile, params) throws FileProcessingException {
        def format = FileFormat.newFormat(downloadFile.type)
        def nativeParams = format.parseRow(params)
        def detection = initDetection(downloadFile, nativeParams)
        assert(detection)

        return detection
    }

    private def initDetection(downloadFile, nativeParams) {
        return nativeParams + [ receiverDownloadId: downloadFile.id ] as Detection
    }
}
