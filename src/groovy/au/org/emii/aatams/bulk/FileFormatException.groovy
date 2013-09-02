package au.org.emii.aatams.bulk


class FileFormatException extends BulkImportException {

    public FileFormatException()
    {
        super()
    }

    public FileFormatException(String message)
    {
        super(message)
    }

    public FileFormatException(String message, Throwable cause)
    {
        super(message, cause)
    }

    public FileFormatException(Throwable cause)
    {
        super(cause)
    }

}
