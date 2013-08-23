package au.org.emii.aatams.bulk

class BulkImportException extends Exception
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
