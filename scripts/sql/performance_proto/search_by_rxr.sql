select
    model_name || '-' || serial_number as receiver_name,
    deploymentdatetime_timestamp as "deployment timestamp",
    valid_detection.timestamp as "detection timestamp",
    recoverydatetime_timestamp as "recovery timestamp",
    installation_station.name as "station name"
      from device

    join device_model on device.model_id = device_model.id
    join receiver_deployment on device.id = receiver_deployment.receiver_id
    join receiver_recovery on receiver_deployment.id = receiver_recovery.deployment_id
    join valid_detection on model_name || '-' || serial_number = valid_detection.receiver_name
    join installation_station on receiver_deployment.station_id = installation_station.id

    where device.class = 'au.org.emii.aatams.Receiver'
    and valid_detection.timestamp between deploymentdatetime_timestamp and recoverydatetime_timestamp

    -- example filters
    and model_name || '-' || serial_number = 'VR2W-101832'
    and installation_station.name = 'MB74A'

    limit 10;
