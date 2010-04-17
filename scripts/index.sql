create index detection_timestamp on aatams.detection(detection_timestamp);
create index detection_tag on aatams.detection(tag_id);
create index detection_deployment on aatams.detection(deployment_id);
create unique index detection_depl_tag_time on aatams.detection(deployment_id,tag_id,detection_timestamp);
create unique index download_file_name on aatams.download_file(download_id,file_name);
create unique index download_device on aatams.download_tag_summary(download_id,device_id);