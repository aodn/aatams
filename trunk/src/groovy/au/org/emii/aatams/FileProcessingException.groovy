package au.org.emii.aatams

/**
 *
 * @author jburgess
 */
class FileProcessingException extends Exception
{
    FileProcessingException()
    {
        super()
    }

    FileProcessingException(String message)
    {
        super(message)
    }

    FileProcessingException(String message, Throwable cause)
    {
        super(message, cause)
    }
    
    FileProcessingException(Throwable cause)
    {
        super(cause)
    }
}

