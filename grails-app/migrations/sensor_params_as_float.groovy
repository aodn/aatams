databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1326155854017-1") {
		modifyDataType(columnName: "intercept", newDataType: "FLOAT4(8,8)", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326155854017-2") {
		modifyDataType(columnName: "slope", newDataType: "FLOAT4(8,8)", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1326155854017-3") {
		modifyDataType(columnName: "initialisationdatetime_timestamp", newDataType: "TIMESTAMP WITH TIME ZONE", tableName: "receiver_deployment")
	}
}
