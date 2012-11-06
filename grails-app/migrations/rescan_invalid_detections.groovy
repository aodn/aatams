import au.org.emii.aatams.detection.DetectionFactoryService;
import org.joda.time.DateTime
import org.joda.time.DateTimeZone;

databaseChangeLog =
{
	changeSet(author: "jburgess", id: "1351571436000-1")
	{
		// rescan for existing recoveries
		grailsChange
		{
			change
			{
				println "Num detections before rescan: " + sql.firstRow("select count(*) as num_rows from valid_detection").num_rows
				println "Num duplicates: " + sql.firstRow('''select count(*) as num_rows from (
																select receiver_name, transmitter_id, timestamp
																from valid_detection
																group by receiver_name, transmitter_id, timestamp
																having count(*) > 1) dups;''').num_rows
				
				def detectionFactoryService = new DetectionFactoryService()
				
				def countRows = sql.firstRow("select count(*) as num_rows from receiver_recovery").num_rows
				def rowsProcessed = 0
				
				sql.eachRow('''select * from receiver_recovery
									join receiver_deployment on receiver_recovery.deployment_id = receiver_deployment.id
									join device on receiver_deployment.receiver_id = device.id
									join device_model on device.model_id = device_model.id''')
				{
					row ->
					
					def receiverName = row.model_name + "-" + row.serial_number
					println("Scanning invalid detections for recovery of receiver ${receiverName} at date/time ${row.recoverydatetime_timestamp}...")
					
					def deployment = 
						[id:  row.deployment_id, 
						 receiver: [name: receiverName], 
						 deploymentDateTime: new DateTime(row.deploymentdatetime_timestamp, DateTimeZone.forID(row.deploymentdatetime_zone)), 
						 recovery: [recoveryDateTime: new DateTime(row.recoverydatetime_timestamp, DateTimeZone.forID(row.recoverydatetime_zone))]]
						
					sql.execute(detectionFactoryService.buildRescanDeploymentSql(deployment))
					
					rowsProcessed++
					
					println("${(int)(rowsProcessed / countRows * 100)}% complete")
				}
				
				println "Num detections after rescan: " + sql.firstRow("select count(*) as num_rows from valid_detection").num_rows
				println "Num duplicates: " + sql.firstRow('''select count(*) as num_rows from (
																select receiver_name, transmitter_id, timestamp
																from valid_detection
																group by receiver_name, transmitter_id, timestamp
																having count(*) > 1) dups;''').num_rows
			}
		}
	}

	// TODO: add index on detection_fk in detection_surgery
	changeSet(author: "jburgess (generated)", id: "1351571436000-2") {
		createIndex(indexName: "valid_detection_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "detection_id")
		}
		createIndex(indexName: "sensor_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "sensor_id")
		}
		createIndex(indexName: "surgery_fk_index", tableName: "detection_surgery", unique: "false") {
			column(name: "surgery_id")
		}
	}
	
	// delete any duplicates
	changeSet(author: "jburgess", id: "1351571436000-3")
	{
		grailsChange
		{
			change
			{
				sql.execute('''
	ALTER TABLE detection_surgery
	DROP CONSTRAINT fkdf258e3b55219160;
  
  ALTER TABLE detection_surgery
	ADD CONSTRAINT fkdf258e3b55219160 FOREIGN KEY (detection_id)
		REFERENCES valid_detection (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE CASCADE;
  
  -- delete goes here --
  DELETE FROM valid_detection
  WHERE id IN (SELECT id
				FROM (SELECT id,
							 row_number() over (partition BY receiver_name, transmitter_id, timestamp ORDER BY id) AS rnum
					   FROM valid_detection) t
				WHERE t.rnum > 1);
  
  ALTER TABLE detection_surgery
	DROP CONSTRAINT fkdf258e3b55219160;
  
  ALTER TABLE detection_surgery
	ADD CONSTRAINT fkdf258e3b55219160 FOREIGN KEY (detection_id)
		REFERENCES valid_detection (id) MATCH SIMPLE
		ON UPDATE NO ACTION ON DELETE NO ACTION;
''')
				
				println "Num detections after deleting duplicates: " + sql.firstRow("select count(*) as num_rows from valid_detection").num_rows
				println "Num duplicates: " + sql.firstRow('''select count(*) as num_rows from (
																select receiver_name, transmitter_id, timestamp
																from valid_detection
																group by receiver_name, transmitter_id, timestamp
																having count(*) > 1) dups;''').num_rows
			}
		}
	}
}
