package au.org.emii.aatams.bulk

import au.org.emii.aatams.FileProcessingException

class BulkImportException extends FileProcessingException
{
    public BulkImportException()
    {
        super()
    }

    public BulkImportException(String message)
    {
        super(message)
    }

    public BulkImportException(String message, Throwable cause)
    {
        super(message, cause)
    }

    public BulkImportException(Throwable cause)
    {
        super(cause)
    }
}
