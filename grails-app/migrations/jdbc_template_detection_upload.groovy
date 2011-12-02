databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1322804613215-1") {
		addDefaultValue(tableName: "detection_surgery", columnName: "id", defaultValueNumeric: "nextval('hibernate_sequence'::regclass)")
        addDefaultValue(tableName: "detection_surgery", columnName: "version", defaultValueNumeric: 0)
		addDefaultValue(tableName: "raw_detection", columnName: "id", defaultValueNumeric: "nextval('hibernate_sequence'::regclass)")
        addDefaultValue(tableName: "raw_detection", columnName: "version", defaultValueNumeric: 0)
    }
}
