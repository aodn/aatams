databaseChangeLog =
{
	changeSet(author: "jburgess", id: "1335235913000-1")
	{
		grailsChange
		{
			change
			{
				sql.eachRow('select id, name from installation_station')
				{
					row ->
					
					sql.executeUpdate("update installation_station set name = '" + row.name.trim() + "' where id = " + row.id)
				}
			}
		}
	}
}

