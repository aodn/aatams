copy (
    select timestamp AT TIME ZONE 'UTC' as "timestamp", station as "station name", latitude, longitude,
        receiver_name as "receiver ID", detection_extract_view_mv.transmitter_id as "tag ID", species_name as "species",
        uploader, detection_extract_view_mv.transmitter_id as "transmitter ID", organisation,
        sensor_value as "sensor value (uncalibrated)", sensor_unit as "sensor unit",
        slope, intercept, (sensor_value * slope + intercept) as "sensor value (calibrated)", unit
        from detection_extract_view_mv, sensor
        where project = 'AATAMS Scott reef'
            and detection_extract_view_mv.transmitter_id = sensor.transmitter_id
    ) to stdout with csv header;
