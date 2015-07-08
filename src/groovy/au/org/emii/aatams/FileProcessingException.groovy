package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
class FileProcessingException extends Exception {
    public FileProcessingException() {
        super()
    }

    public FileProcessingException(String message) {
        super(message)
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause)
    }

    public FileProcessingException(Throwable cause) {
        super(cause)
    }
}

