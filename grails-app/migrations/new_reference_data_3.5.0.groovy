databaseChangeLog = 
{
	changeSet(author: "jburgess", id: "1320969080-1") 
	{
		insert(tableName: "animal_measurement_type")
		{
			column(name:"id", valueNumeric:44)
			column(name:"version", valueNumeric:0)
			column(name:"type", value:"TOTAL LENGTH")
		}
		
		insert(tableName: "animal_measurement_type")
		{
			column(name:"id", valueNumeric:45)
			column(name:"version", valueNumeric:0)
			column(name:"type", value:"CARAPACE LENGTH")
		}
		
		insert(tableName: "animal_measurement_type")
		{
			column(name:"id", valueNumeric:46)
			column(name:"version", valueNumeric:0)
			column(name:"type", value:"FORK LENGTH")
		}
	}
	
	changeSet(author: "jburgess", id: "1320969080-2") 
	{
		insert(tableName: "capture_method")
		{
			column(name:"id", valueNumeric:44)
			column(name:"version", valueNumeric:0)
			column(name:"name", value:"ELECTROFISHING")
		}
		
		insert(tableName: "capture_method")
		{
			column(name:"id", valueNumeric:45)
			column(name:"version", valueNumeric:0)
			column(name:"name", value:"HATCHERY-REARED")
		}
	}
	
	changeSet(author: "jburgess", id: "1320969080-3") 
	{
		insert(tableName: "device_model")
		{
			column(name:"id", valueNumeric:25)
			column(name:"version", valueNumeric:0)
			column(name:"manufacturer_id", valueNumeric:14)
			column(name:"model_name", value:"V13AP")
			column(name:"class", value:"au.org.emii.aatams.TagDeviceModel")
		}
		
		insert(tableName: "device_model")
		{
			column(name:"id", valueNumeric:26)
			column(name:"version", valueNumeric:0)
			column(name:"manufacturer_id", valueNumeric:14)
			column(name:"model_name", value:"V16AP")
			column(name:"class", value:"au.org.emii.aatams.TagDeviceModel")
		}
	}
}
