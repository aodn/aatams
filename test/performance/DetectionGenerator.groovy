import java.text.SimpleDateFormat

/**
 * Generates detection data in CSV format (for performance testing).
 */

// 
// Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude
// 2010-01-01 12:34:56,0,A69-1303-0

def header = "Date and Time (UTC),Receiver,Transmitter,Transmitter Name,Transmitter Serial,Sensor Value,Sensor Unit,Station Name,Latitude,Longitude"
def receiverPrefix = ""
def transmitterPrefix = "A69-1303-"
def delimiter = ','

def numReceivers = 1
def numTransmitters = 12
def numDetections = 1000 //10 //100   // (per receiver/tag combination)

def DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss"
def dateFormat = new SimpleDateFormat(DATE_FORMAT_STRING)
def dateIncrementS = 10
def currDate = dateFormat.parse("2002-01-01 10:20:30")
def calendar = Calendar.getInstance()
calendar.setTime(currDate)

def filePath = "/Users/jburgess/Dropbox/IMOS/aatams/performance/detections.csv"

File outFile = new File(filePath)
outFile.write(header)
outFile.append('\n')

numDetections.times
{
    // For each receiver...
    numReceivers.times
    {
        def receiverId = receiverPrefix + it
        
        numTransmitters.times
        {
            transmitterIndex ->
            
            def transmitterId = transmitterPrefix + transmitterIndex

            StringBuilder line = new StringBuilder(dateFormat.format(calendar.getTime()))
            line.append(delimiter)
            line.append(receiverId)
            line.append(delimiter)
            line.append(transmitterId)

            // Remaining fields are null
            line.append('\n')
            
            outFile.append(line.toString())
            
            // Increment the date, so that each detection has a unique date.
            calendar.add(Calendar.SECOND, dateIncrementS)
        }
    }
}
