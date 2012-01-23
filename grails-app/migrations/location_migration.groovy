import com.vividsolutions.jts.geom.Point

databaseChangeLog = 
{
	changeSet(author: "jburgess", id: "1327275795000-1") 
	{
		[[tableName:"animal_release", columnName: "capture_location"],
		[tableName:"animal_release", columnName: "release_location"],
		[tableName:"raw_detection", columnName: "location"],
		[tableName:"installation_station", columnName: "location"],
		[tableName:"receiver_deployment", columnName: "location"],
		[tableName:"receiver_recovery", columnName: "location"]].each
		{
			params ->
			
			// rename
			renameColumn(tableName:params.tableName, oldColumnName:params.columnName, newColumnName:"location_as_jts")
			
			// add column
			sql("SELECT AddGeometryColumn('" + params.tableName + "', '" + params.columnName + "', 4326, 'GEOMETRY', 2)")
			
			// migrate/convert
			grailsChange
			{
				change
				{
					def locationMap = [:]
					
					sql.eachRow('select id, location_as_jts from ' + params.tableName)
					{
						row ->
						
						locationMap[row.id] = row.location_as_jts
					}
					
					locationMap.each
					{
						k, v ->
						
						if (v)	// location may be null.
						{
							ByteArrayInputStream locationAsByteStream = new ByteArrayInputStream(v)
							ObjectInputStream locationAsObjectStream = new ObjectInputStream(locationAsByteStream)
							Point locationAsPoint = (Point)locationAsObjectStream.readObject()
							
							def id = k
							def lon = locationAsPoint.x
							def lat = locationAsPoint.y
							def srid = locationAsPoint.SRID
							
							sql.executeUpdate "update " + params.tableName + " set " + params.columnName + "=ST_GeomFromText('POINT(" + lon + " " + lat + ")', $srid) where id = $id"
						}
					}
				}
			}
			
			// delete column
			dropColumn(tableName:params.tableName, columnName:"location_as_jts")
		}
	}
}
