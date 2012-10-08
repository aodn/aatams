package au.org.emii.aatams.bulk

class BulkImportException extends Exception
{
    BulkImportException()
    {
        super()
    }

    BulkImportException(String message)
    {
        super(message)
    }

    BulkImportException(String message, Throwable cause)
    {
        super(message, cause)
    }
    
    BulkImportException(Throwable cause)
    {
        super(cause)
    }
}
