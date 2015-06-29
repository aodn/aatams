package au.org.emii.aatams.event

import au.org.emii.aatams.FileFormat
import au.org.emii.aatams.FileFormatException

class EventFormat extends FileFormat
{
    static final String DATE_AND_TIME_COLUMN = "Date/Time"
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss Z"
    static final String RECEIVER_COLUMN = "Receiver"
    static final String DESCRIPTION_COLUMN = "Description"
    static final String DATA_COLUMN = "Data"
    static final String UNITS_COLUMN = "Units"
    static final List UNITS_DETAILS_COLUMNS = ["Units detail 1", "Units detail 2", "Units detail 3"]

    Map parseRow(row) throws FileFormatException
    {
        def timestamp = getUtcDate(row, DATE_AND_TIME_COLUMN, DATE_FORMAT)

        // nullable string properties which are blank are saved as null, so do that here too
        // so that the comparison when checking for duplicates works properly.
        def data = row[DATA_COLUMN] == "" ? null : row[DATA_COLUMN]
        def units = row[UNITS_COLUMN] == "" ? null : row[UNITS_COLUMN]

        UNITS_DETAILS_COLUMNS.each
        {
            columnName ->

            if (row[columnName])
            {
                units += "," + row[columnName]
            }
        }

        return [timestamp: timestamp,
                receiverName: row[RECEIVER_COLUMN],
                description: row[DESCRIPTION_COLUMN],
                data: data,
                units: units]
    }
}
