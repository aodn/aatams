import com.vividsolutions.jts.geom.Point

databaseChangeLog =
{
	changeSet(author: "jburgess", id: "1328843757000-1")
	{
		[[tableName:"animal_release", columnName: "capture_location"],
		[tableName:"animal_release", columnName: "release_location"],
		[tableName:"raw_detection", columnName: "location"],
		[tableName:"installation_station", columnName: "location"],
		[tableName:"receiver_deployment", columnName: "location"],
		[tableName:"receiver_recovery", columnName: "location"]].each
		{
			params ->
			
			// migrate/convert
			grailsChange
			{
				change
				{
					sql.eachRow('select id, st_x(' + params.columnName + ') as lon, st_y(' + params.columnName + ') as lat, st_srid(' + params.columnName + ') as srid from ' + params.tableName + ' where ' + params.columnName + ' is not NULL')
					{
						row ->
						
						def lat = 0 - Math.abs(row.lat)	// Ensure latitude is South.
						def lon = row.lon
						def srid = row.srid
						def id = row.id
						
						sql.executeUpdate("update " + params.tableName + " set " + params.columnName + "=ST_GeomFromText('POINT(" + lon + " " + lat + ")', " + srid + ") where id = $id")
						
					}
				}
			}
		}
	}
}

