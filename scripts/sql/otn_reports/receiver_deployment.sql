COPY (
    select

      to_char(deploymentdatetime_timestamp AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SSZ') as "deployment date/time",
      model_name as "receiver model name",
      trim(serial_number) as "receiver serial number",
      installation_station.name as "station name",
      type as "mooring type",
      acoustic_releaseid as "acoustic release id",
      battery_life_days as "battery life (days)",
      bottom_depthm as "bottom depth (m)",
      comments,
      depth_below_surfacem as "depth below surface (m)",
      st_y(installation_station.location) as "latitude",
      st_x(installation_station.location) as "longitude",
      mooring_descriptor as "mooring descriptor",
      receiver_orientation as "receiver orientation",
      to_char(recovery_date AT TIME ZONE 'UTC', 'YYYY-MM-DD') as "scheduled recovery date",
      installation.name as "installation name"


    from receiver_deployment
    inner join device on device.id = receiver_deployment.receiver_id
    inner join device_model on device_model.id = device.model_id
    inner join installation_station on installation_station.id = receiver_deployment.station_id
    inner join installation on installation.id = installation_station.installation_id
    inner join mooring_type on mooring_type.id = receiver_deployment.mooring_type_id

    where installation.name in ('Maria Island Line', 'OTN Perth Line')
) TO STDOUT

    QUOTE '"' CSV HEADER
     --  QUOTE AS '@'
     -- CSV
     --  HEADER
     --  DELIMITER ','
