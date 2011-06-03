package au.org.emii.aatams.upload

class FileProcessorService {

    static transactional = true

    def process(fileId)
    {
        log.info "Start of processing, file ID: " + fileId
        Thread.sleep(5000)
        log.info "Finished processing."
    }
}
