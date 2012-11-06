delete from valid_detection v1 USING valid_detection v2
where v1.receiver_name = v2.receiver_name and v1.transmitter_id = v2.transmitter_id and v1.timestamp = v2.timestamp
and v1.id < v2.id;
