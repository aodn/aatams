import au.org.emii.aatams.*
import au.org.emii.aatams.detection.*

databaseChangeLog =
{
	changeSet(author: "jburgess", id: "1329279127000-1")
	{
		def findDuplicatesStatement = '''select * from device
where trim(serial_number) in
(
	select trim(serial_number) 
	from device 
	where class = 'au.org.emii.aatams.Receiver'
	group by trim(serial_number)
	having (count(trim(serial_number)) > 1)
)

order by trim(serial_number), id
'''
		
		grailsChange
		{
			change
			{
				long rowCount = 0
				def rxrToKeepId
				def rxrToKeepSerialNumber
		
				sql.eachRow(findDuplicatesStatement)
				{
					row ->
					
					if (rowCount % 2 == 0)
					{
						// Keep this row.
						rxrToKeepId = row.id
						rxrToKeepSerialNumber = row.serial_number.trim()
					}
					else
					{
						// Merge this row in to previous.
						assert(rxrToKeepSerialNumber == row.serial_number.trim())
						
						// FKs
						sql.execute('update receiver_deployment set receiver_id = ' + rxrToKeepId + ' where receiver_id = ' + row.id)
						sql.execute('update receiver_raw_detection set receiver_detections_id = ' + rxrToKeepId + ' where receiver_detections_id = ' + row.id)
						sql.execute('update installation_station_receiver set receiver_id = ' + rxrToKeepId + ' where receiver_id = ' + row.id)
						
						sql.execute('delete from device where id = ' + row.id)
					}
					
					rowCount++
				}
			}
		}
	}
}
