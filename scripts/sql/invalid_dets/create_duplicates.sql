insert into valid_detection 
	select (id + 1000000), version, location, receiver_deployment_id, receiver_download_id, receiver_name, sensor_unit, 
	       sensor_value, station_name, timestamp, transmitter_id, transmitter_name, transmitter_serial_number from valid_detection where receiver_name = 'VR2W-109079';

