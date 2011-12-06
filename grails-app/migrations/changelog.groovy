databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1318566800479-1") {
		createTable(tableName: "address") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "address_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "country", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "postcode", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "state", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "street_address", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "suburb_town", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-2") {
		createTable(tableName: "animal") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "animal_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sex_id", type: "int8")

			column(name: "species_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-3") {
		createTable(tableName: "animal_measurement") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "animal_measurement_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comments", type: "TEXT")

			column(name: "estimate", type: "bool") {
				constraints(nullable: "false")
			}

			column(name: "release_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "unit_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "value", type: "FLOAT4(8,8)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-4") {
		createTable(tableName: "animal_measurement_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "animal_measurement_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-5") {
		createTable(tableName: "animal_release") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "animal_release_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "animal_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "capturedatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "capturedatetime_zone", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "capture_locality", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "capture_location", type: "bytea") {
				constraints(nullable: "false")
			}

			column(name: "capture_method_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comments", type: "TEXT")

			column(name: "embargo_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "project_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "releasedatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "releasedatetime_zone", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "release_locality", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "release_location", type: "bytea") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-6") {
		createTable(tableName: "capture_method") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "capture_method_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-7") {
		createTable(tableName: "detection_surgery") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "detection_surgery_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "detection_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "surgery_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "tag_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "detection_surgeries_idx", type: "int4")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-8") {
		createTable(tableName: "device") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "device_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "comment", type: "VARCHAR(255)")

			column(name: "model_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "serial_number", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "status_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8")

			column(name: "code_map", type: "VARCHAR(255)")

			column(name: "expected_life_time_days", type: "int4")

			column(name: "ping_code", type: "int4")

			column(name: "project_id", type: "int8")

			column(name: "transmitter_type_id", type: "int8")

			column(name: "intercept", type: "int4")

			column(name: "slope", type: "int4")

			column(name: "tag_id", type: "int8")

			column(name: "unit", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-9") {
		createTable(tableName: "device_manufacturer") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "device_manufacturer_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "manufacturer_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-10") {
		createTable(tableName: "device_model") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "device_model_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "manufacturer_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "model_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-11") {
		createTable(tableName: "device_status") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "device_status_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-12") {
		createTable(tableName: "geometry_columns") {
			column(name: "f_table_catalog", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "f_table_schema", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "f_table_name", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "f_geometry_column", type: "VARCHAR(256)") {
				constraints(nullable: "false")
			}

			column(name: "coord_dimension", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "srid", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(30)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-13") {
		createTable(tableName: "installation") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "installation_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "configuration_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "project_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-14") {
		createTable(tableName: "installation_configuration") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "installation_configuration_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-15") {
		createTable(tableName: "installation_station") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "installation_station_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "curtain_position", type: "int4")

			column(name: "installation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "bytea") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "num_deployments", type: "int4") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-16") {
		createTable(tableName: "installation_station_receiver") {
			column(name: "installation_station_receivers_id", type: "int8")

			column(name: "receiver_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-17") {
		createTable(tableName: "measurement_unit") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "measurement_unit_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "unit", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-18") {
		createTable(tableName: "mooring_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "mooring_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-19") {
		createTable(tableName: "notification") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "notification_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "anchor_selector", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "html_fragment", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "key", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "unauthenticated", type: "bool") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-20") {
		createTable(tableName: "notification_person") {
			column(name: "notification_acknowledgers_id", type: "int8")

			column(name: "person_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-21") {
		createTable(tableName: "organisation") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "organisation_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "department", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "fax_number", type: "VARCHAR(255)")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "phone_number", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "postal_address_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "requesting_user_id", type: "int8")

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "street_address_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-22") {
		createTable(tableName: "organisation_project") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "organisation_project_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "project_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-23") {
		createTable(tableName: "project") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "project_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "description", type: "VARCHAR(255)")

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "requesting_user_id", type: "int8")

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-24") {
		createTable(tableName: "project_role") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "project_role_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "access", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "person_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "project_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "role_type_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-25") {
		createTable(tableName: "project_role_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "project_role_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "display_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-26") {
		createTable(tableName: "raw_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "raw_detection_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "bytea")

			column(name: "receiver_download_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "sensor_unit", type: "VARCHAR(255)")

			column(name: "sensor_value", type: "FLOAT4(8,8)")

			column(name: "station_name", type: "VARCHAR(255)")

			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_id", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_name", type: "VARCHAR(255)")

			column(name: "transmitter_serial_number", type: "VARCHAR(255)")

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "receiver_deployment_id", type: "int8")

			column(name: "message", type: "VARCHAR(255)")

			column(name: "reason", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-27") {
		createTable(tableName: "receiver_deployment") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_deployment_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "acoustic_releaseid", type: "VARCHAR(255)")

			column(name: "battery_life_days", type: "int4")

			column(name: "bottom_depthm", type: "FLOAT4(8,8)")

			column(name: "comments", type: "TEXT")

			column(name: "deploymentdatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "deploymentdatetime_zone", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "deployment_number", type: "int4")

			column(name: "depth_below_surfacem", type: "FLOAT4(8,8)")

			column(name: "embargo_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "location", type: "bytea")

			column(name: "mooring_descriptor", type: "VARCHAR(255)")

			column(name: "mooring_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_orientation", type: "VARCHAR(255)")

			column(name: "recovery_id", type: "int8")

			column(name: "recovery_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "station_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-28") {
		createTable(tableName: "receiver_deployment_raw_detection") {
			column(name: "receiver_deployment_detections_id", type: "int8")

			column(name: "raw_detection_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-29") {
		createTable(tableName: "receiver_download_file") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_download_file_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "err_msg", type: "TEXT") {
				constraints(nullable: "false")
			}

			column(name: "import_date", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "path", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "requesting_user_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-30") {
		createTable(tableName: "receiver_event") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_event_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "data", type: "VARCHAR(255)")

			column(name: "description", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "receiver_deployment_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_download_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "units", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-31") {
		createTable(tableName: "receiver_raw_detection") {
			column(name: "receiver_detections_id", type: "int8")

			column(name: "raw_detection_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-32") {
		createTable(tableName: "receiver_recovery") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_recovery_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comments", type: "TEXT")

			column(name: "location", type: "bytea") {
				constraints(nullable: "false")
			}

			column(name: "recoverer_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "recoverydatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "recoverydatetime_zone", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "status_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-33") {
		createTable(tableName: "sec_role") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sec_role_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-34") {
		createTable(tableName: "sec_role_permissions") {
			column(name: "sec_role_id", type: "int8")

			column(name: "permissions_string", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-35") {
		createTable(tableName: "sec_user") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sec_user_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "password_hash", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "username", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "default_time_zone", type: "bytea")

			column(name: "email_address", type: "VARCHAR(255)")

			column(name: "name", type: "VARCHAR(255)")

			column(name: "organisation_id", type: "int8")

			column(name: "phone_number", type: "VARCHAR(255)")

			column(name: "registration_comment", type: "VARCHAR(255)")

			column(name: "status", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-36") {
		createTable(tableName: "sec_user_permissions") {
			column(name: "sec_user_id", type: "int8")

			column(name: "permissions_string", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-37") {
		createTable(tableName: "sec_user_roles") {
			column(name: "sec_role_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sec_user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-38") {
		createTable(tableName: "sex") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sex_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sex", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-39") {
		createTable(tableName: "spatial_ref_sys") {
			column(name: "srid", type: "int4") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "spatial_ref_sys_pkey")
			}

			column(name: "auth_name", type: "VARCHAR(256)")

			column(name: "auth_srid", type: "int4")

			column(name: "srtext", type: "VARCHAR(2048)")

			column(name: "proj4text", type: "VARCHAR(2048)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-40") {
		createTable(tableName: "species") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "species_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "embargo_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "name", type: "VARCHAR(255)")

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "assigned_family_code", type: "VARCHAR(255)")

			column(name: "assigned_family_sequence", type: "VARCHAR(255)")

			column(name: "authority", type: "VARCHAR(255)")

			column(name: "common_name", type: "VARCHAR(255)")

			column(name: "common_names_list", type: "VARCHAR(255)")

			column(name: "date_last_modified", type: "VARCHAR(255)")

			column(name: "family", type: "VARCHAR(255)")

			column(name: "family_sequence", type: "VARCHAR(255)")

			column(name: "genus", type: "VARCHAR(255)")

			column(name: "habitat_code", type: "VARCHAR(255)")

			column(name: "infraorder", type: "VARCHAR(255)")

			column(name: "kingdom", type: "VARCHAR(255)")

			column(name: "obis_classification_code", type: "VARCHAR(255)")

			column(name: "order_name", type: "VARCHAR(255)")

			column(name: "phylum", type: "VARCHAR(255)")

			column(name: "recent_synonyms", type: "VARCHAR(255)")

			column(name: "scientific_name", type: "VARCHAR(255)")

			column(name: "sciname_informal", type: "VARCHAR(255)")

			column(name: "spclass", type: "VARCHAR(255)")

			column(name: "spcode", type: "VARCHAR(255)")

			column(name: "species", type: "VARCHAR(255)")

			column(name: "subclass", type: "VARCHAR(255)")

			column(name: "subgenus", type: "VARCHAR(255)")

			column(name: "suborder", type: "VARCHAR(255)")

			column(name: "subphylum", type: "VARCHAR(255)")

			column(name: "subspecies", type: "VARCHAR(255)")

			column(name: "undescribed_sp_flag", type: "VARCHAR(255)")

			column(name: "variety", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-41") {
		createTable(tableName: "surgery") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "surgery_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comments", type: "TEXT")

			column(name: "release_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "tag_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp_timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "timestamp_zone", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "treatment_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "surgeries_idx", type: "int4")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-42") {
		createTable(tableName: "surgery_treatment_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "surgery_treatment_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-43") {
		createTable(tableName: "surgery_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "surgery_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-44") {
		createTable(tableName: "transmitter_type") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "transmitter_type_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_type_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-45") {
		createTable(tableName: "ufile") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "ufile_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "date_uploaded", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "downloads", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "extension", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "path", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "size", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-46") {
		addPrimaryKey(columnNames: "f_table_catalog, f_table_schema, f_table_name, f_geometry_column", constraintName: "geometry_columns_pk", tableName: "geometry_columns")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-47") {
		addPrimaryKey(columnNames: "sec_user_id, sec_role_id", constraintName: "sec_user_roles_pkey", tableName: "sec_user_roles")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-48") {
		addUniqueConstraint(columnNames: "type", constraintName: "animal_measurement_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "animal_measurement_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-49") {
		addUniqueConstraint(columnNames: "ping_code", constraintName: "device_ping_code_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-50") {
		addUniqueConstraint(columnNames: "serial_number", constraintName: "device_serial_number_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-51") {
		addUniqueConstraint(columnNames: "manufacturer_name", constraintName: "device_manufacturer_manufacturer_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_manufacturer")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-52") {
		addUniqueConstraint(columnNames: "model_name", constraintName: "device_model_model_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_model")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-53") {
		addUniqueConstraint(columnNames: "status", constraintName: "device_status_status_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_status")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-54") {
		addUniqueConstraint(columnNames: "type", constraintName: "installation_configuration_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "installation_configuration")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-55") {
		addUniqueConstraint(columnNames: "unit", constraintName: "measurement_unit_unit_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "measurement_unit")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-56") {
		addUniqueConstraint(columnNames: "type", constraintName: "mooring_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "mooring_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-57") {
		addUniqueConstraint(columnNames: "key", constraintName: "notification_key_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "notification")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-58") {
		addUniqueConstraint(columnNames: "name", constraintName: "project_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "project")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-59") {
		addUniqueConstraint(columnNames: "display_name", constraintName: "project_role_type_display_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "project_role_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-60") {
		addUniqueConstraint(columnNames: "name", constraintName: "sec_role_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sec_role")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-61") {
		addUniqueConstraint(columnNames: "username", constraintName: "sec_user_username_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sec_user")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-62") {
		addUniqueConstraint(columnNames: "sex", constraintName: "sex_sex_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sex")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-63") {
		addUniqueConstraint(columnNames: "spcode", constraintName: "species_spcode_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "species")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-64") {
		addUniqueConstraint(columnNames: "type", constraintName: "surgery_treatment_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "surgery_treatment_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-65") {
		addUniqueConstraint(columnNames: "type", constraintName: "surgery_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "surgery_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-66") {
		addUniqueConstraint(columnNames: "transmitter_type_name", constraintName: "transmitter_type_transmitter_type_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "transmitter_type")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-67") {
		createIndex(indexName: "code_name_index", tableName: "device", unique: "false") {
			column(name: "code_name")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-68") {
		createIndex(indexName: "timestamp_index", tableName: "raw_detection", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-69") {
		addForeignKeyConstraint(baseColumnNames: "sex_id", baseTableName: "animal", baseTableSchemaName: "public", constraintName: "fkabc58dfccd365681", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sex", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-70") {
		addForeignKeyConstraint(baseColumnNames: "species_id", baseTableName: "animal", baseTableSchemaName: "public", constraintName: "fkabc58dfc94348341", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "species", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-71") {
		addForeignKeyConstraint(baseColumnNames: "release_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf9b5c10085", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_release", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-72") {
		addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf914018681", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_measurement_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-73") {
		addForeignKeyConstraint(baseColumnNames: "unit_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf9e6ea591d", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "measurement_unit", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-74") {
		addForeignKeyConstraint(baseColumnNames: "animal_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c4e0347853", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-75") {
		addForeignKeyConstraint(baseColumnNames: "capture_method_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c45d50d0e", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "capture_method", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-76") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c4bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-77") {
		addForeignKeyConstraint(baseColumnNames: "detection_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bea110b34", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "raw_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-78") {
		addForeignKeyConstraint(baseColumnNames: "surgery_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3b96ba9ca1", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-79") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-80") {
		addForeignKeyConstraint(baseColumnNames: "model_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e561c043beb", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_model", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-81") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e5699b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-82") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-83") {
		addForeignKeyConstraint(baseColumnNames: "status_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e566b73e7c9", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_status", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-84") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56ceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-85") {
		addForeignKeyConstraint(baseColumnNames: "transmitter_type_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e564b0c3bc4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "transmitter_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-86") {
		addForeignKeyConstraint(baseColumnNames: "manufacturer_id", baseTableName: "device_model", baseTableSchemaName: "public", constraintName: "fkdcc4e40025f67029", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_manufacturer", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-87") {
		addForeignKeyConstraint(baseColumnNames: "configuration_id", baseTableName: "installation", baseTableSchemaName: "public", constraintName: "fk796d5e3a18d65d27", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_configuration", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-88") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "installation", baseTableSchemaName: "public", constraintName: "fk796d5e3abf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-89") {
		addForeignKeyConstraint(baseColumnNames: "installation_id", baseTableName: "installation_station", baseTableSchemaName: "public", constraintName: "fk902c2c2f35e870d3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-90") {
		addForeignKeyConstraint(baseColumnNames: "installation_station_receivers_id", baseTableName: "installation_station_receiver", baseTableSchemaName: "public", constraintName: "fk2f7233ffd6ed9307", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_station", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-91") {
		addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "installation_station_receiver", baseTableSchemaName: "public", constraintName: "fk2f7233fff0bb6d33", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-92") {
		addForeignKeyConstraint(baseColumnNames: "notification_acknowledgers_id", baseTableName: "notification_person", baseTableSchemaName: "public", constraintName: "fk41244129fafc8826", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "notification", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-93") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "notification_person", baseTableSchemaName: "public", constraintName: "fk41244129e985cdb3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-94") {
		addForeignKeyConstraint(baseColumnNames: "postal_address_id", baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300dac7e435", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "address", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-95") {
		addForeignKeyConstraint(baseColumnNames: "requesting_user_id", baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300daa7db8c31", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-96") {
		addForeignKeyConstraint(baseColumnNames: "street_address_id", baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300dabc5dddfd", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "address", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-97") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "organisation_project", baseTableSchemaName: "public", constraintName: "fkf3b978b499b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-98") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "organisation_project", baseTableSchemaName: "public", constraintName: "fkf3b978b4bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-99") {
		addForeignKeyConstraint(baseColumnNames: "requesting_user_id", baseTableName: "project", baseTableSchemaName: "public", constraintName: "fked904b19a7db8c31", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-100") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dce985cdb3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-101") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dcbf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-102") {
		addForeignKeyConstraint(baseColumnNames: "role_type_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dc51218487", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project_role_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-103") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "raw_detection", baseTableSchemaName: "public", constraintName: "fk8f206b2eb30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-104") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "raw_detection", baseTableSchemaName: "public", constraintName: "fk8f206b2e7eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-105") {
		addForeignKeyConstraint(baseColumnNames: "mooring_type_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb1531eebdc", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "mooring_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-106") {
		addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb15f0bb6d33", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-107") {
		addForeignKeyConstraint(baseColumnNames: "recovery_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb157c50c982", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_recovery", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-108") {
		addForeignKeyConstraint(baseColumnNames: "station_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb15cdaf3227", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_station", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-109") {
		addForeignKeyConstraint(baseColumnNames: "raw_detection_id", baseTableName: "receiver_deployment_raw_detection", baseTableSchemaName: "public", constraintName: "fk9ea19f84e9e13deb", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "raw_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-110") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_detections_id", baseTableName: "receiver_deployment_raw_detection", baseTableSchemaName: "public", constraintName: "fk9ea19f847197496f", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-111") {
		addForeignKeyConstraint(baseColumnNames: "requesting_user_id", baseTableName: "receiver_download_file", baseTableSchemaName: "public", constraintName: "fk3b462883a7db8c31", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-112") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "receiver_event", baseTableSchemaName: "public", constraintName: "fk224d504ab30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-113") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "receiver_event", baseTableSchemaName: "public", constraintName: "fk224d504a7eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-114") {
		addForeignKeyConstraint(baseColumnNames: "raw_detection_id", baseTableName: "receiver_raw_detection", baseTableSchemaName: "public", constraintName: "fkc88698dee9e13deb", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "raw_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-115") {
		addForeignKeyConstraint(baseColumnNames: "receiver_detections_id", baseTableName: "receiver_raw_detection", baseTableSchemaName: "public", constraintName: "fkc88698de92eec5e4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-116") {
		addForeignKeyConstraint(baseColumnNames: "recoverer_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e5dc30b5bf", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-117") {
		addForeignKeyConstraint(baseColumnNames: "status_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e56b73e7c9", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_status", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-118") {
		addForeignKeyConstraint(baseColumnNames: "sec_role_id", baseTableName: "sec_role_permissions", baseTableSchemaName: "public", constraintName: "fk103984696511cce0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-119") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "sec_user", baseTableSchemaName: "public", constraintName: "fk375df2f999b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-120") {
		addForeignKeyConstraint(baseColumnNames: "sec_user_id", baseTableName: "sec_user_permissions", baseTableSchemaName: "public", constraintName: "fkbf3983ea3c90c0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-121") {
		addForeignKeyConstraint(baseColumnNames: "sec_role_id", baseTableName: "sec_user_roles", baseTableSchemaName: "public", constraintName: "fk3e2705f76511cce0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-122") {
		addForeignKeyConstraint(baseColumnNames: "sec_user_id", baseTableName: "sec_user_roles", baseTableSchemaName: "public", constraintName: "fk3e2705f7a3c90c0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-123") {
		addForeignKeyConstraint(baseColumnNames: "release_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f5b5c10085", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_release", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-124") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f5ceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-125") {
		addForeignKeyConstraint(baseColumnNames: "treatment_type_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f54f2dd3af", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery_treatment_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-126") {
		addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f56d95e296", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-127") {
		createSequence(schemaName: "public", sequenceName: "hibernate_sequence")
	}

	changeSet(author: "jburgess (generated)", id: "1318566800479-128") {
		createView("SELECT current_database() AS f_table_catalog, n.nspname AS f_table_schema, c.relname AS f_table_name, a.attname AS f_geography_column, geography_typmod_dims(a.atttypmod) AS coord_dimension, geography_typmod_srid(a.atttypmod) AS srid, geography_typmod_type(a.atttypmod) AS type FROM pg_class c, pg_attribute a, pg_type t, pg_namespace n WHERE ((((((t.typname = 'geography'::name) AND (a.attisdropped = false)) AND (a.atttypid = t.oid)) AND (a.attrelid = c.oid)) AND (c.relnamespace = n.oid)) AND (NOT pg_is_other_temp_schema(c.relnamespace)));", schemaName: "public", viewName: "geography_columns")
	}
	
	changeSet(author: "jburgess (generated)", id: "1318566460667-1") {
		createIndex(indexName: "organisation_status_index", tableName: "organisation", unique: "false") {
			column(name: "status")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566460667-2") {
		createIndex(indexName: "project_status_index", tableName: "project", unique: "false") {
			column(name: "status")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1318566460667-3") {
		createIndex(indexName: "device_status_index", tableName: "device", unique: "false") {
			column(name: "status_id")
		}
   	}

	include file: 'deployment_has_one_recovery.groovy'

	include file: 'requester_refactor.groovy'

	include file: '3_4_1_changes.groovy'

	include file: 'caab_attributes_to_lowercase.groovy'

	include file: 'introduce_code_map_table.groovy'
	
	include file: 'new_reference_data_3.5.0.groovy'

	include file: 'jdbc_template_detection_upload.groovy'
}

