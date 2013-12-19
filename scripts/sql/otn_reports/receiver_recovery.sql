COPY (
    select

      to_char(deploymentdatetime_timestamp AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SSZ') as "deployment date/time",
      model_name as "receiver model name",
      trim(serial_number) as "receiver serial number",
      installation_station.name as "station name",
      to_char(initialisationdatetime_timestamp AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SSZ') as "initialisation date/time",
      st_y(installation_station.location) as "latitude",
      st_x(installation_station.location) as "longitude",
      sec_user.name as "recoverer name",
      to_char(recoverydatetime_timestamp AT TIME ZONE 'UTC', 'YYYY-MM-DD"T"HH24:MI:SSZ') as "recovery date/time",
      device_status.status,
      receiver_recovery.comments

    from receiver_deployment
    join receiver_recovery on receiver_recovery.deployment_id = receiver_deployment.id
    join device on device.id = receiver_deployment.receiver_id
    join device_model on device_model.id = device.model_id
    join installation_station on installation_station.id = receiver_deployment.station_id
    join installation on installation.id = installation_station.installation_id
    join mooring_type on mooring_type.id = receiver_deployment.mooring_type_id
    join project_role on receiver_recovery.recoverer_id = project_role.id
    join sec_user on sec_user.id = project_role.person_id
    join device_status on device_status.id = receiver_recovery.status_id

    where installation.name in ('Maria Island Line', 'OTN Perth Line')

    order by deploymentdatetime_timestamp

) TO STDOUT

    WITH CSV HEADER QUOTE '"'
