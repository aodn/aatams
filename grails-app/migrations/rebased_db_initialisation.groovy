databaseChangeLog = {

	changeSet(author: "jburgess (generated)", id: "1371625156687-1") {
		createTable(tableName: "aatams_acoustictag_all_deployments_mv") {
			column(name: "funding_type", type: "TEXT")

			column(name: "project_name", type: "VARCHAR(255)")

			column(name: "installation_name", type: "VARCHAR(255)")

			column(name: "station_name", type: "VARCHAR(255)")

			column(name: "no_deployments", type: "int4")

			column(name: "no_detections", type: "int8")

			column(name: "station_lat_lon", type: "TEXT")

			column(name: "deployment_depth", type: "DECIMAL")

			column(name: "start_date", type: "DATE")

			column(name: "end_date", type: "DATE")

			column(name: "coverage_duration", type: "DECIMAL")

			column(name: "station_lat", type: "DECIMAL")

			column(name: "station_lon", type: "DECIMAL")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-2") {
		createTable(tableName: "aatams_acoustictag_all_detections_view_mv") {
			column(name: "phylum", type: "VARCHAR(255)")

			column(name: "order_name", type: "VARCHAR(255)")

			column(name: "spcode", type: "VARCHAR(255)")

			column(name: "common_name", type: "VARCHAR(255)")

			column(name: "scientific_name", type: "VARCHAR(255)")

			column(name: "no_releases", type: "int8")

			column(name: "no_detections", type: "int8")

			column(name: "first_detection", type: "DATE")

			column(name: "last_detection", type: "DATE")

			column(name: "coverage_duration", type: "FLOAT8(17)")

			column(name: "missing_info", type: "TEXT")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-3") {
		createTable(tableName: "aatams_acoustictag_totals_species_view_mv") {
			column(name: "statistics", type: "DECIMAL")

			column(name: "statistics_type", type: "TEXT")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-4") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-2") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-3") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-4") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-5") {
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

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "capture_location", type: "geometry")

			column(name: "release_location", type: "geometry")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-6") {
		createTable(tableName: "audit_log") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "audit_log_pkey")
			}

			column(name: "actor", type: "VARCHAR(255)")

			column(name: "class_name", type: "VARCHAR(255)")

			column(name: "date_created", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "event_name", type: "VARCHAR(255)")

			column(name: "last_updated", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "new_value", type: "VARCHAR(255)")

			column(name: "old_value", type: "VARCHAR(255)")

			column(name: "persisted_object_id", type: "VARCHAR(255)")

			column(name: "persisted_object_version", type: "int8")

			column(name: "property_name", type: "VARCHAR(255)")

			column(name: "uri", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-7") {
		createTable(tableName: "bulk_import") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "bulk_import_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "filename", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "import_finish_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "import_start_date", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-8") {
		createTable(tableName: "bulk_import_record") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "bulk_import_record_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "bulk_import_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "dst_class", type: "VARCHAR(255)")

			column(name: "dst_pk", type: "int8")

			column(name: "src_modified_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "src_pk", type: "int8")

			column(name: "src_table", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-9") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-10") {
		createTable(tableName: "code_map") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "code_map_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "code_map", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-11") {
		createTable(tableName: "detection_count_per_station_mv") {
			column(name: "station", type: "VARCHAR(255)")

			column(name: "installation", type: "VARCHAR(255)")

			column(name: "project", type: "VARCHAR(255)")

			column(name: "public_location", type: "geometry")

			column(name: "public_lon", type: "FLOAT8(17)")

			column(name: "public_lat", type: "FLOAT8(17)")

			column(name: "installation_station_url", type: "TEXT")

			column(name: "detection_download_url", type: "TEXT")

			column(name: "detection_count", type: "int8")

			column(name: "relative_detection_count", type: "FLOAT8(17)")

			column(name: "station_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-12") {
		createTable(tableName: "detection_extract_view_mv") {
			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "formatted_timestamp", type: "TEXT")

			column(name: "station", type: "VARCHAR(255)")

			column(name: "station_id", type: "int8")

			column(name: "location", type: "geometry")

			column(name: "latitude", type: "FLOAT8(17)")

			column(name: "longitude", type: "FLOAT8(17)")

			column(name: "receiver_name", type: "TEXT")

			column(name: "sensor_id", type: "VARCHAR(255)")

			column(name: "species_name", type: "TEXT")

			column(name: "uploader", type: "VARCHAR(255)")

			column(name: "transmitter_id", type: "VARCHAR(255)")

			column(name: "organisation", type: "VARCHAR(255)")

			column(name: "project", type: "VARCHAR(255)")

			column(name: "installation", type: "VARCHAR(255)")

			column(name: "spcode", type: "VARCHAR(255)")

			column(name: "animal_release_id", type: "int8")

			column(name: "embargo_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "project_id", type: "int8")

			column(name: "detection_id", type: "int8")

			column(name: "release_project_id", type: "int8")

			column(name: "sensor_value", type: "FLOAT4(8,8)")

			column(name: "sensor_unit", type: "VARCHAR(255)")

			column(name: "provisional", type: "bool")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-13") {
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

			column(name: "sensor_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-14") {
		createTable(tableName: "device") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "device_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comment", type: "VARCHAR(255)")

			column(name: "model_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "serial_number", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "status_id", type: "int8")

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8")

			column(name: "expected_life_time_days", type: "int4")

			column(name: "project_id", type: "int8")

			column(name: "code_map_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-15") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-16") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-17") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-19") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-20") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-21") {
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

			column(name: "name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "num_deployments", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-22") {
		createTable(tableName: "installation_station_receiver") {
			column(name: "installation_station_receivers_id", type: "int8")

			column(name: "receiver_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-23") {
		createTable(tableName: "invalid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "invalid_detection_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry")

			column(name: "message", type: "VARCHAR(255)")

			column(name: "reason", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

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
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-24") {
		createTable(tableName: "matviews") {
			column(name: "mv_name", type: "NAME") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "matviews_pkey")
			}

			column(name: "v_name", type: "NAME") {
				constraints(nullable: "false")
			}

			column(name: "last_refresh", type: "TIMESTAMP WITH TIME ZONE")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-25") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-26") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-27") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-28") {
		createTable(tableName: "notification_person") {
			column(name: "notification_acknowledgers_id", type: "int8")

			column(name: "person_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-29") {
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

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "street_address_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-30") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-31") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-32") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-33") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-34") {
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

			column(name: "mooring_descriptor", type: "VARCHAR(255)")

			column(name: "mooring_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "receiver_orientation", type: "VARCHAR(255)")

			column(name: "recovery_date", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "station_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "initialisationdatetime_timestamp", type: "TIMESTAMP WITH TIME ZONE")

			column(name: "initialisationdatetime_zone", type: "VARCHAR(255)")

			column(name: "location", type: "geometry")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-35") {
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

			column(name: "requesting_user_id", type: "int8")

			column(name: "status", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "type", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-36") {
		createTable(tableName: "receiver_download_file_progress") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_download_file_progress_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "percent_complete", type: "int4")

			column(name: "receiver_download_file_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-37") {
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

			column(name: "receiver_deployment_id", type: "int8")

			column(name: "receiver_download_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "timestamp", type: "TIMESTAMP WITH TIME ZONE") {
				constraints(nullable: "false")
			}

			column(name: "units", type: "VARCHAR(255)")

			column(name: "class", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "VARCHAR(255)")

			column(name: "reason", type: "VARCHAR(255)")

			column(name: "receiver_name", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-38") {
		createTable(tableName: "receiver_recovery") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "receiver_recovery_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "comments", type: "TEXT")

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

			column(defaultValueNumeric: "0", name: "deployment_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-39") {
		createTable(tableName: "receiver_valid_detection") {
			column(name: "receiver_detections_id", type: "int8")

			column(name: "valid_detection_id", type: "int8")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-40") {
		createTable(tableName: "request") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "request_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "organisation_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "requester_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-41") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-42") {
		createTable(tableName: "sec_role_permissions") {
			column(name: "sec_role_id", type: "int8")

			column(name: "permissions_string", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-43") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-44") {
		createTable(tableName: "sec_user_permissions") {
			column(name: "sec_user_id", type: "int8")

			column(name: "permissions_string", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-45") {
		createTable(tableName: "sec_user_roles") {
			column(name: "sec_role_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "sec_user_id", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-46") {
		createTable(tableName: "sensor") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "sensor_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "intercept", type: "FLOAT4(8,8)")

			column(name: "ping_code", type: "int4") {
				constraints(nullable: "false")
			}

			column(name: "slope", type: "FLOAT4(8,8)")

			column(name: "tag_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "transmitter_type_id", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "unit", type: "VARCHAR(255)")

			column(name: "transmitter_id", type: "VARCHAR(255)")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-47") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-49") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-50") {
		createTable(tableName: "statistics") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "statistics_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "key", type: "VARCHAR(255)") {
				constraints(nullable: "false")
			}

			column(name: "value", type: "int8") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-51") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-52") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-53") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-54") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-55") {
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

	changeSet(author: "jburgess (generated)", id: "1371620272075-56") {
		createTable(tableName: "valid_detection") {
			column(name: "id", type: "int8") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "valid_detection_pkey")
			}

			column(name: "version", type: "int8") {
				constraints(nullable: "false")
			}

			column(name: "location", type: "geometry")

			column(name: "receiver_deployment_id", type: "int8") {
				constraints(nullable: "false")
			}

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

			column(defaultValueBoolean: "false", name: "provisional", type: "bool") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-58") {
		addPrimaryKey(columnNames: "sec_user_id, sec_role_id", constraintName: "sec_user_roles_pkey", tableName: "sec_user_roles")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-59") {
		addUniqueConstraint(columnNames: "type", constraintName: "animal_measurement_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "animal_measurement_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-60") {
		addUniqueConstraint(columnNames: "code_map", constraintName: "code_map_code_map_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "code_map")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-61") {
		addUniqueConstraint(columnNames: "model_id, serial_number", constraintName: "device_model_id_serial_number_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-62") {
		addUniqueConstraint(columnNames: "manufacturer_name", constraintName: "device_manufacturer_manufacturer_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_manufacturer")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-63") {
		addUniqueConstraint(columnNames: "model_name", constraintName: "device_model_model_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_model")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-64") {
		addUniqueConstraint(columnNames: "status", constraintName: "device_status_status_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "device_status")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-65") {
		addUniqueConstraint(columnNames: "type", constraintName: "installation_configuration_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "installation_configuration")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-66") {
		addUniqueConstraint(columnNames: "unit", constraintName: "measurement_unit_unit_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "measurement_unit")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-67") {
		addUniqueConstraint(columnNames: "type", constraintName: "mooring_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "mooring_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-68") {
		addUniqueConstraint(columnNames: "key", constraintName: "notification_key_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "notification")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-69") {
		addUniqueConstraint(columnNames: "name", constraintName: "project_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "project")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-70") {
		addUniqueConstraint(columnNames: "display_name", constraintName: "project_role_type_display_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "project_role_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-71") {
		addUniqueConstraint(columnNames: "receiver_download_file_id", constraintName: "receiver_download_file_progress_receiver_download_file_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "receiver_download_file_progress")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-72") {
		addUniqueConstraint(columnNames: "deployment_id", constraintName: "receiver_recovery_deployment_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "receiver_recovery")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-73") {
		addUniqueConstraint(columnNames: "organisation_id", constraintName: "request_organisation_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "request")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-74") {
		addUniqueConstraint(columnNames: "name", constraintName: "sec_role_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sec_role")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-75") {
		addUniqueConstraint(columnNames: "username", constraintName: "sec_user_username_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sec_user")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-76") {
		addUniqueConstraint(columnNames: "tag_id, transmitter_type_id", constraintName: "sensor_tag_id_transmitter_type_id_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sensor")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-77") {
		addUniqueConstraint(columnNames: "sex", constraintName: "sex_sex_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "sex")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-78") {
		addUniqueConstraint(columnNames: "key", constraintName: "statistics_key_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "statistics")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-79") {
		addUniqueConstraint(columnNames: "type", constraintName: "surgery_treatment_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "surgery_treatment_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-80") {
		addUniqueConstraint(columnNames: "type", constraintName: "surgery_type_type_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "surgery_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-81") {
		addUniqueConstraint(columnNames: "transmitter_type_name", constraintName: "transmitter_type_transmitter_type_name_key", deferrable: "false", disabled: "false", initiallyDeferred: "false", tableName: "transmitter_type")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-82") {
		createIndex(indexName: "detection_extract_view_mv_installation_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "installation")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-83") {
		createIndex(indexName: "detection_extract_view_mv_project_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "project")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-84") {
		createIndex(indexName: "detection_extract_view_mv_provisional_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "provisional")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-85") {
		createIndex(indexName: "detection_extract_view_mv_spcode_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "spcode")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-86") {
		createIndex(indexName: "detection_extract_view_mv_station_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "station")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-87") {
		createIndex(indexName: "detection_extract_view_mv_timestamp_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-88") {
		createIndex(indexName: "detection_extract_view_mv_transmitter_id_index", tableName: "detection_extract_view_mv", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-89") {
		createIndex(indexName: "invalid_detection_reason_index", tableName: "invalid_detection", unique: "false") {
			column(name: "reason")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-90") {
		createIndex(indexName: "invalid_receivername_index", tableName: "invalid_detection", unique: "false") {
			column(name: "receiver_name")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-91") {
		createIndex(indexName: "invalid_timestamp_index", tableName: "invalid_detection", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-92") {
		createIndex(indexName: "invalid_transmitterid_index", tableName: "invalid_detection", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-93") {
		createIndex(indexName: "organisation_status_index", tableName: "organisation", unique: "false") {
			column(name: "status")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-94") {
		createIndex(indexName: "project_status_index", tableName: "project", unique: "false") {
			column(name: "status")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-95") {
		createIndex(indexName: "event_timestamp_index", tableName: "receiver_event", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-96") {
		createIndex(indexName: "ping_code_idx", tableName: "sensor", unique: "false") {
			column(name: "ping_code")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-97") {
		createIndex(indexName: "spcode_index", tableName: "species", unique: "false") {
			column(name: "spcode")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-98") {
		createIndex(indexName: "valid_provisional_index", tableName: "valid_detection", unique: "false") {
			column(name: "provisional")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-99") {
		createIndex(indexName: "valid_receivername_index", tableName: "valid_detection", unique: "false") {
			column(name: "receiver_name")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-100") {
		createIndex(indexName: "valid_timestamp_index", tableName: "valid_detection", unique: "false") {
			column(name: "timestamp")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-101") {
		createIndex(indexName: "valid_transmitterid_index", tableName: "valid_detection", unique: "false") {
			column(name: "transmitter_id")
		}
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-102") {
		addForeignKeyConstraint(baseColumnNames: "sex_id", baseTableName: "animal", baseTableSchemaName: "public", constraintName: "fkabc58dfccd365681", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sex", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-103") {
		addForeignKeyConstraint(baseColumnNames: "species_id", baseTableName: "animal", baseTableSchemaName: "public", constraintName: "fkabc58dfc94348341", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "species", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-104") {
		addForeignKeyConstraint(baseColumnNames: "release_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf9b5c10085", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_release", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-105") {
		addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf914018681", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_measurement_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-106") {
		addForeignKeyConstraint(baseColumnNames: "unit_id", baseTableName: "animal_measurement", baseTableSchemaName: "public", constraintName: "fk8eb3adf9e6ea591d", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "measurement_unit", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-107") {
		addForeignKeyConstraint(baseColumnNames: "animal_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c4e0347853", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-108") {
		addForeignKeyConstraint(baseColumnNames: "capture_method_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c45d50d0e", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "capture_method", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-109") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "animal_release", baseTableSchemaName: "public", constraintName: "fk88d6a0c4bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-110") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "bulk_import", baseTableSchemaName: "public", constraintName: "fkf7eb277299b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-111") {
		addForeignKeyConstraint(baseColumnNames: "bulk_import_id", baseTableName: "bulk_import_record", baseTableSchemaName: "public", constraintName: "fk53875dbec6323f02", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "bulk_import", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-112") {
		addForeignKeyConstraint(baseColumnNames: "detection_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3b55219160", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "valid_detection", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-113") {
		addForeignKeyConstraint(baseColumnNames: "sensor_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3bd7d8b793", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sensor", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-114") {
		addForeignKeyConstraint(baseColumnNames: "surgery_id", baseTableName: "detection_surgery", baseTableSchemaName: "public", constraintName: "fkdf258e3b96ba9ca1", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-115") {
		addForeignKeyConstraint(baseColumnNames: "code_map_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56472540a6", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "code_map", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-116") {
		addForeignKeyConstraint(baseColumnNames: "model_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e561c043beb", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_model", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-117") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e5699b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-118") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e56bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-119") {
		addForeignKeyConstraint(baseColumnNames: "status_id", baseTableName: "device", baseTableSchemaName: "public", constraintName: "fkb06b1e566b73e7c9", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_status", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-120") {
		addForeignKeyConstraint(baseColumnNames: "manufacturer_id", baseTableName: "device_model", baseTableSchemaName: "public", constraintName: "fkdcc4e40025f67029", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_manufacturer", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-121") {
		addForeignKeyConstraint(baseColumnNames: "configuration_id", baseTableName: "installation", baseTableSchemaName: "public", constraintName: "fk796d5e3a18d65d27", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_configuration", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-122") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "installation", baseTableSchemaName: "public", constraintName: "fk796d5e3abf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-123") {
		addForeignKeyConstraint(baseColumnNames: "installation_id", baseTableName: "installation_station", baseTableSchemaName: "public", constraintName: "fk902c2c2f35e870d3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-124") {
		addForeignKeyConstraint(baseColumnNames: "installation_station_receivers_id", baseTableName: "installation_station_receiver", baseTableSchemaName: "public", constraintName: "fk2f7233ffd6ed9307", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_station", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-125") {
		addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "installation_station_receiver", baseTableSchemaName: "public", constraintName: "fk2f7233fff0bb6d33", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-126") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "invalid_detection", baseTableSchemaName: "public", constraintName: "fk2b27ee3d7eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-127") {
		addForeignKeyConstraint(baseColumnNames: "notification_acknowledgers_id", baseTableName: "notification_person", baseTableSchemaName: "public", constraintName: "fk41244129fafc8826", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "notification", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-128") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "notification_person", baseTableSchemaName: "public", constraintName: "fk41244129e985cdb3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-129") {
		addForeignKeyConstraint(baseColumnNames: "postal_address_id", baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300dac7e435", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "address", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-130") {
		addForeignKeyConstraint(baseColumnNames: "street_address_id", baseTableName: "organisation", baseTableSchemaName: "public", constraintName: "fk3a5300dabc5dddfd", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "address", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-131") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "organisation_project", baseTableSchemaName: "public", constraintName: "fkf3b978b499b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-132") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "organisation_project", baseTableSchemaName: "public", constraintName: "fkf3b978b4bf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-133") {
		addForeignKeyConstraint(baseColumnNames: "requesting_user_id", baseTableName: "project", baseTableSchemaName: "public", constraintName: "fked904b19a7db8c31", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-134") {
		addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dce985cdb3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-135") {
		addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dcbf505a21", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-136") {
		addForeignKeyConstraint(baseColumnNames: "role_type_id", baseTableName: "project_role", baseTableSchemaName: "public", constraintName: "fk37fff5dc51218487", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project_role_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-137") {
		addForeignKeyConstraint(baseColumnNames: "mooring_type_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb1531eebdc", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "mooring_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-138") {
		addForeignKeyConstraint(baseColumnNames: "receiver_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb15f0bb6d33", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-139") {
		addForeignKeyConstraint(baseColumnNames: "station_id", baseTableName: "receiver_deployment", baseTableSchemaName: "public", constraintName: "fk862aeb15cdaf3227", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "installation_station", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-140") {
		addForeignKeyConstraint(baseColumnNames: "requesting_user_id", baseTableName: "receiver_download_file", baseTableSchemaName: "public", constraintName: "fk3b462883a7db8c31", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-141") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_file_id", baseTableName: "receiver_download_file_progress", baseTableSchemaName: "public", constraintName: "fk5875ae6950da4c63", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-142") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "receiver_event", baseTableSchemaName: "public", constraintName: "fk224d504ab30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-143") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "receiver_event", baseTableSchemaName: "public", constraintName: "fk224d504a7eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-144") {
		addForeignKeyConstraint(baseColumnNames: "deployment_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e579a96182", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-145") {
		addForeignKeyConstraint(baseColumnNames: "recoverer_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e5dc30b5bf", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "project_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-146") {
		addForeignKeyConstraint(baseColumnNames: "status_id", baseTableName: "receiver_recovery", baseTableSchemaName: "public", constraintName: "fk82de83e56b73e7c9", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device_status", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-147") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "request", baseTableSchemaName: "public", constraintName: "fk414ef28f99b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-148") {
		addForeignKeyConstraint(baseColumnNames: "requester_id", baseTableName: "request", baseTableSchemaName: "public", constraintName: "fk414ef28f4ad0c2c", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-149") {
		addForeignKeyConstraint(baseColumnNames: "sec_role_id", baseTableName: "sec_role_permissions", baseTableSchemaName: "public", constraintName: "fk103984696511cce0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-150") {
		addForeignKeyConstraint(baseColumnNames: "organisation_id", baseTableName: "sec_user", baseTableSchemaName: "public", constraintName: "fk375df2f999b5ecd3", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "organisation", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-151") {
		addForeignKeyConstraint(baseColumnNames: "sec_user_id", baseTableName: "sec_user_permissions", baseTableSchemaName: "public", constraintName: "fkbf3983ea3c90c0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-152") {
		addForeignKeyConstraint(baseColumnNames: "sec_role_id", baseTableName: "sec_user_roles", baseTableSchemaName: "public", constraintName: "fk3e2705f76511cce0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_role", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-153") {
		addForeignKeyConstraint(baseColumnNames: "sec_user_id", baseTableName: "sec_user_roles", baseTableSchemaName: "public", constraintName: "fk3e2705f7a3c90c0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "sec_user", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-154") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "sensor", baseTableSchemaName: "public", constraintName: "fkca0053baceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-155") {
		addForeignKeyConstraint(baseColumnNames: "transmitter_type_id", baseTableName: "sensor", baseTableSchemaName: "public", constraintName: "fkca0053ba4b0c3bc4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "transmitter_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-156") {
		addForeignKeyConstraint(baseColumnNames: "release_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f5b5c10085", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "animal_release", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-157") {
		addForeignKeyConstraint(baseColumnNames: "tag_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f5ceab1a01", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "device", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-158") {
		addForeignKeyConstraint(baseColumnNames: "treatment_type_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f54f2dd3af", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery_treatment_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-159") {
		addForeignKeyConstraint(baseColumnNames: "type_id", baseTableName: "surgery", baseTableSchemaName: "public", constraintName: "fk918a71f56d95e296", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "surgery_type", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-160") {
		addForeignKeyConstraint(baseColumnNames: "receiver_deployment_id", baseTableName: "valid_detection", baseTableSchemaName: "public", constraintName: "fk28ce8a02b30f8f32", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_deployment", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-161") {
		addForeignKeyConstraint(baseColumnNames: "receiver_download_id", baseTableName: "valid_detection", baseTableSchemaName: "public", constraintName: "fk28ce8a027eb9cbee", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "id", referencedTableName: "receiver_download_file", referencedTableSchemaName: "public", referencesUniqueColumn: "false")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-162") {
		createSequence(schemaName: "public", sequenceName: "detection_surgery_sequence")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-163") {
		createSequence(schemaName: "public", sequenceName: "hibernate_sequence")
	}

	changeSet(author: "jburgess", id: "1371620272000-1", runOnChange: true)
	{
		createProcedure('''CREATE OR REPLACE FUNCTION isreleaseembargoed(release_id bigint) RETURNS boolean AS
			$$
			DECLARE
				found_animal_release animal_release%ROWTYPE;
			BEGIN
				SELECT INTO found_animal_release * FROM animal_release WHERE id = release_id;
				RETURN found_animal_release.embargo_date > current_date;
			END;
			$$ LANGUAGE plpgsql STRICT;''')
	}
	
	changeSet(author: "jburgess", id: "1371620272000-2", runOnChange: true)
	{
		createProcedure('''CREATE OR REPLACE FUNCTION scramblepoint(point geometry) RETURNS geometry AS
			$$
			DECLARE
				scrambled_point geometry;
				scrambled_lat numeric;
				scrambled_lon numeric;
			BEGIN
				scrambled_lon = round(st_x(point)::NUMERIC, 2);
				scrambled_lat = round(st_y(point)::NUMERIC, 2);

				return st_point(scrambled_lon, scrambled_lat);
			END;
			$$ LANGUAGE plpgsql STRICT;''')
	}
    
	changeSet(author: "jburgess", id: "1371620272000-3", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION create_matview(NAME, NAME)
						RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS
						$$
						DECLARE
							matview ALIAS FOR $1;
							view_name ALIAS FOR $2;
							entry matviews%ROWTYPE;
						BEGIN
							SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
							IF FOUND THEN
								RAISE EXCEPTION 'Materialized view % already exists.', matview;
							END IF;
						
							EXECUTE 'REVOKE ALL ON ' || view_name || ' FROM PUBLIC';
						
							EXECUTE 'GRANT SELECT ON ' || view_name || ' TO PUBLIC';
						
							EXECUTE 'CREATE TABLE ' || matview || ' AS SELECT * FROM ' || view_name;
						
							EXECUTE 'REVOKE ALL ON ' || matview || ' FROM PUBLIC';
						
							EXECUTE 'GRANT SELECT ON ' || matview || ' TO PUBLIC';
						
							INSERT INTO matviews (mv_name, v_name, last_refresh)
							  VALUES (matview, view_name, CURRENT_TIMESTAMP);
							
							RETURN;
						END;
						$$''')
	}
	
	changeSet(author: "jburgess", id: "1371620272000-4", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION drop_matview(NAME) RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS 
						$$
						DECLARE
						    matview ALIAS FOR $1;
						    entry matviews%ROWTYPE;
						BEGIN
						
						    SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
						    IF NOT FOUND THEN
						        RAISE EXCEPTION 'Materialized view % does not exist.', matview;
						    END IF;
						
						    EXECUTE 'DROP TABLE ' || matview;
						    DELETE FROM matviews WHERE mv_name=matview;
						
						    RETURN;
						END;
						$$''')
	}

	changeSet(author: "jburgess", id: "1371620272000-5", runOnChange: true)
	{
		createProcedure('''
						CREATE OR REPLACE FUNCTION refresh_matview(name) RETURNS VOID
						SECURITY DEFINER
						LANGUAGE plpgsql AS 
						$$
						DECLARE 
						    matview ALIAS FOR $1;
						    entry matviews%ROWTYPE;
						BEGIN
						
						    SELECT * INTO entry FROM matviews WHERE mv_name = matview;
						
						    IF NOT FOUND THEN
						         RAISE EXCEPTION 'Materialized view % does not exist.', matview;
						    END IF;
						
						    EXECUTE 'DELETE FROM ' || matview;
						    EXECUTE 'INSERT INTO ' || matview
						        || ' SELECT * FROM ' || entry.v_name;
						
						    UPDATE matviews
						        SET last_refresh=CURRENT_TIMESTAMP
						        WHERE mv_name=matview;
						
						    RETURN;
						END;
						$$''')
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-167") {
		createView("SELECT aatams_acoustictag_all_deployments_mv.funding_type, aatams_acoustictag_all_deployments_mv.project_name, aatams_acoustictag_all_deployments_mv.installation_name, aatams_acoustictag_all_deployments_mv.station_name, CASE WHEN (aatams_acoustictag_all_deployments_mv.no_deployments IS NULL) THEN 0 ELSE aatams_acoustictag_all_deployments_mv.no_deployments END AS no_deployments, CASE WHEN ((aatams_acoustictag_all_deployments_mv.no_deployments IS NULL) OR (aatams_acoustictag_all_deployments_mv.no_deployments = 0)) THEN (0)::bigint ELSE aatams_acoustictag_all_deployments_mv.no_detections END AS no_detections, aatams_acoustictag_all_deployments_mv.station_lat_lon, aatams_acoustictag_all_deployments_mv.deployment_depth, aatams_acoustictag_all_deployments_mv.start_date, aatams_acoustictag_all_deployments_mv.end_date, aatams_acoustictag_all_deployments_mv.coverage_duration, aatams_acoustictag_all_deployments_mv.station_lat, aatams_acoustictag_all_deployments_mv.station_lon FROM aatams_acoustictag_all_deployments_mv;", schemaName: "public", viewName: "aatams_acoustictag_all_deployments_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-168") {
		createView("SELECT aatams_acoustictag_all_deployments_view.funding_type, aatams_acoustictag_all_deployments_view.project_name, (count(DISTINCT aatams_acoustictag_all_deployments_view.installation_name))::numeric AS no_installations, (count(DISTINCT aatams_acoustictag_all_deployments_view.station_name))::numeric AS no_stations, (sum(aatams_acoustictag_all_deployments_view.no_deployments))::numeric AS no_deployments, sum(aatams_acoustictag_all_deployments_view.no_detections) AS no_detections, COALESCE(((min(aatams_acoustictag_all_deployments_view.station_lat) || '/'::text) || max(aatams_acoustictag_all_deployments_view.station_lat))) AS lat_range, COALESCE(((min(aatams_acoustictag_all_deployments_view.station_lon) || '/'::text) || max(aatams_acoustictag_all_deployments_view.station_lon))) AS lon_range, COALESCE(((min(aatams_acoustictag_all_deployments_view.deployment_depth) || '-'::text) || max(aatams_acoustictag_all_deployments_view.deployment_depth))) AS deployment_depth_range, min(aatams_acoustictag_all_deployments_view.start_date) AS start_date, max(aatams_acoustictag_all_deployments_view.end_date) AS end_date, ((max(aatams_acoustictag_all_deployments_view.end_date) - min(aatams_acoustictag_all_deployments_view.start_date)))::numeric AS coverage_duration, min(aatams_acoustictag_all_deployments_view.station_lat) AS min_lat, max(aatams_acoustictag_all_deployments_view.station_lat) AS max_lat, min(aatams_acoustictag_all_deployments_view.station_lon) AS min_lon, max(aatams_acoustictag_all_deployments_view.station_lon) AS max_lon, min(aatams_acoustictag_all_deployments_view.deployment_depth) AS min_depth, max(aatams_acoustictag_all_deployments_view.deployment_depth) AS max_depth FROM aatams_acoustictag_all_deployments_view GROUP BY aatams_acoustictag_all_deployments_view.funding_type, aatams_acoustictag_all_deployments_view.project_name ORDER BY aatams_acoustictag_all_deployments_view.funding_type DESC, aatams_acoustictag_all_deployments_view.project_name;", schemaName: "public", viewName: "aatams_acoustictag_data_summary_project_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-169") {
		createView("SELECT aatams_acoustictag_all_detections_view_mv.phylum, aatams_acoustictag_all_detections_view_mv.order_name, aatams_acoustictag_all_detections_view_mv.spcode, aatams_acoustictag_all_detections_view_mv.common_name, aatams_acoustictag_all_detections_view_mv.scientific_name, aatams_acoustictag_all_detections_view_mv.no_releases, aatams_acoustictag_all_detections_view_mv.no_detections, aatams_acoustictag_all_detections_view_mv.first_detection, aatams_acoustictag_all_detections_view_mv.last_detection, aatams_acoustictag_all_detections_view_mv.coverage_duration, aatams_acoustictag_all_detections_view_mv.missing_info FROM aatams_acoustictag_all_detections_view_mv;", schemaName: "public", viewName: "aatams_acoustictag_data_summary_species_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-170") {
		createView("SELECT aatams_acoustictag_data_summary_project_view.funding_type, count(DISTINCT aatams_acoustictag_data_summary_project_view.project_name) AS no_projects, sum(aatams_acoustictag_data_summary_project_view.no_installations) AS no_installations, sum(aatams_acoustictag_data_summary_project_view.no_stations) AS no_stations, sum(aatams_acoustictag_data_summary_project_view.no_deployments) AS no_deployments, sum(aatams_acoustictag_data_summary_project_view.no_detections) AS no_detections FROM aatams_acoustictag_data_summary_project_view GROUP BY aatams_acoustictag_data_summary_project_view.funding_type UNION ALL SELECT 'TOTAL' AS funding_type, count(DISTINCT aatams_acoustictag_data_summary_project_view.project_name) AS no_projects, sum(aatams_acoustictag_data_summary_project_view.no_installations) AS no_installations, sum(aatams_acoustictag_data_summary_project_view.no_stations) AS no_stations, sum(aatams_acoustictag_data_summary_project_view.no_deployments) AS no_deployments, sum(aatams_acoustictag_data_summary_project_view.no_detections) AS no_detections FROM aatams_acoustictag_data_summary_project_view ORDER BY 1;", schemaName: "public", viewName: "aatams_acoustictag_totals_project_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-171") {
		createView("SELECT aatams_acoustictag_totals_species_view_mv.statistics, aatams_acoustictag_totals_species_view_mv.statistics_type FROM aatams_acoustictag_totals_species_view_mv;", schemaName: "public", viewName: "aatams_acoustictag_totals_species_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-174") {
		createView("SELECT count(project.name) AS count, project.name FROM ((((valid_detection JOIN receiver_deployment ON ((valid_detection.receiver_deployment_id = receiver_deployment.id))) JOIN installation_station ON ((receiver_deployment.station_id = installation_station.id))) JOIN installation ON ((installation_station.installation_id = installation.id))) JOIN project ON ((installation.project_id = project.id))) GROUP BY project.name ORDER BY count(project.name) DESC;", schemaName: "public", viewName: "detections_by_project")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-175") {
		createView("SELECT count(*) AS count, species.common_name, species.scientific_name, species.spcode FROM (((((valid_detection JOIN detection_surgery ON ((valid_detection.id = detection_surgery.detection_id))) JOIN surgery ON ((detection_surgery.surgery_id = surgery.id))) JOIN animal_release ON ((surgery.release_id = animal_release.id))) JOIN animal ON ((animal_release.animal_id = animal.id))) JOIN species ON ((animal.species_id = species.id))) GROUP BY species.spcode, species.scientific_name, species.common_name ORDER BY count(*) DESC;", schemaName: "public", viewName: "detections_by_species")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-177") {
		createView('''SELECT installation.name AS "installation name" FROM installation ORDER BY installation.name;''', schemaName: "public", viewName: "installation_list")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-178") {
		createView('''SELECT project.name AS "project name", count(project.name) AS "number of stations" FROM ((installation_station JOIN installation ON ((installation_station.installation_id = installation.id))) JOIN project ON ((installation.project_id = project.id))) GROUP BY project.name ORDER BY project.name;''', schemaName: "public", viewName: "project_list")
	}

	changeSet(author: "jburgess (generated)", id: "1371625156687-180") {
		createView('''SELECT count(species.spcode) AS count, species.spcode AS "CAAB code", species.common_name, species.scientific_name FROM ((animal_release JOIN animal ON ((animal_release.animal_id = animal.id))) JOIN species ON ((animal.species_id = species.id))) GROUP BY species.spcode, species.common_name, species.scientific_name ORDER BY count(species.spcode) DESC;''', schemaName: "public", viewName: "releases_by_species")
    }
    
	changeSet(author: "jburgess (generated)", id: "1371620272075-164") {
		createView('''SELECT valid_detection."timestamp", to_char(timezone('00:00'::text, valid_detection."timestamp"), 'YYYY-MM-DD HH24:MI:SS'::text) AS formatted_timestamp, installation_station.name AS station, installation_station.id AS station_id, installation_station.location, st_y(installation_station.location) AS latitude, st_x(installation_station.location) AS longitude, (((device_model.model_name)::text || '-'::text) || (device.serial_number)::text) AS receiver_name, COALESCE(sensor.transmitter_id, ''::character varying) AS sensor_id, COALESCE(((((((species.spcode)::text || ' - '::text) || (species.scientific_name)::text) || ' ('::text) || (species.common_name)::text) || ')'::text), ''::text) AS species_name, sec_user.name AS uploader, valid_detection.transmitter_id, organisation.name AS organisation, project.name AS project, installation.name AS installation, COALESCE(species.spcode, ''::character varying) AS spcode, animal_release.id AS animal_release_id, animal_release.embargo_date, project.id AS project_id, valid_detection.id AS detection_id, animal_release.project_id AS release_project_id, valid_detection.sensor_value, valid_detection.sensor_unit, valid_detection.provisional FROM (((((((((((((((valid_detection LEFT JOIN receiver_deployment ON ((valid_detection.receiver_deployment_id = receiver_deployment.id))) LEFT JOIN installation_station ON ((receiver_deployment.station_id = installation_station.id))) LEFT JOIN installation ON ((installation_station.installation_id = installation.id))) LEFT JOIN project ON ((installation.project_id = project.id))) LEFT JOIN device ON ((receiver_deployment.receiver_id = device.id))) LEFT JOIN device_model ON ((device.model_id = device_model.id))) LEFT JOIN receiver_download_file ON ((valid_detection.receiver_download_id = receiver_download_file.id))) LEFT JOIN sec_user ON ((receiver_download_file.requesting_user_id = sec_user.id))) LEFT JOIN organisation ON ((device.organisation_id = organisation.id))) LEFT JOIN detection_surgery ON ((valid_detection.id = detection_surgery.detection_id))) LEFT JOIN sensor ON ((detection_surgery.sensor_id = sensor.id))) LEFT JOIN surgery ON ((detection_surgery.surgery_id = surgery.id))) LEFT JOIN animal_release ON ((surgery.release_id = animal_release.id))) LEFT JOIN animal ON ((animal_release.animal_id = animal.id))) LEFT JOIN species ON ((animal.species_id = species.id)));''', schemaName: "public", viewName: "detection_extract_view")
	}

	changeSet(author: "jburgess (generated)", id: "1371620272075-165") {
		createView('''SELECT detection_extract_view."timestamp", detection_extract_view.formatted_timestamp, detection_extract_view.station, detection_extract_view.station_id, detection_extract_view.location, detection_extract_view.latitude, detection_extract_view.longitude, detection_extract_view.receiver_name, detection_extract_view.sensor_id, detection_extract_view.species_name, detection_extract_view.uploader, detection_extract_view.transmitter_id, detection_extract_view.organisation, detection_extract_view.project, detection_extract_view.installation, detection_extract_view.spcode, detection_extract_view.animal_release_id, detection_extract_view.embargo_date, detection_extract_view.project_id, detection_extract_view.detection_id, scramblepoint(detection_extract_view.location) AS public_location, CASE WHEN isreleaseembargoed(detection_extract_view.animal_release_id) THEN 'embargoed'::text ELSE detection_extract_view.species_name END AS public_species_name, CASE WHEN isreleaseembargoed(detection_extract_view.animal_release_id) THEN 'embargoed'::character varying ELSE detection_extract_view.spcode END AS public_spcode FROM detection_extract_view;''', schemaName: "public", viewName: "public_detection_view")
	}
    
	changeSet(author: "jburgess (generated)", id: "1371620272075-166") {
		createView("SELECT public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, st_x(public_detection_view.public_location) AS public_lon, st_y(public_detection_view.public_location) AS public_lat, ('http://localhost:8080/aatams/installationStation/show/'::text || public_detection_view.station_id) AS installation_station_url, ('http://localhost:8080/aatams/detection/list?filter.receiverDeployment.station.in=name&filter.receiverDeployment.station.in='::text || (public_detection_view.station)::text) AS detection_download_url, count(*) AS detection_count, ((log((GREATEST(count(*), (1)::bigint))::double precision) / log(((SELECT max(t.detection_count) AS max FROM (SELECT public_detection_view.station, count(public_detection_view.station) AS detection_count FROM public_detection_view GROUP BY public_detection_view.station) t))::double precision)) * (10)::double precision) AS relative_detection_count, public_detection_view.station_id FROM public_detection_view GROUP BY public_detection_view.station, public_detection_view.installation, public_detection_view.project, public_detection_view.public_location, public_detection_view.station_id UNION ALL SELECT installation_station.name AS station, installation.name AS installation, project.name AS project, scramblepoint(installation_station.location) AS public_location, st_x(scramblepoint(installation_station.location)) AS public_lon, st_y(scramblepoint(installation_station.location)) AS public_lat, ('http://localhost:8080/aatams/installationStation/show/'::text || installation_station.id) AS installation_station_url, '' AS detection_download_url, 0 AS detection_count, 0 AS relative_detection_count, installation_station.id AS station_id FROM (((installation_station LEFT JOIN installation ON ((installation_station.installation_id = installation.id))) LEFT JOIN project ON ((installation.project_id = project.id))) LEFT JOIN public_detection_view ON ((installation_station.id = public_detection_view.station_id))) WHERE (public_detection_view.station_id IS NULL);", schemaName: "public", viewName: "detection_count_per_station")
	}
}
