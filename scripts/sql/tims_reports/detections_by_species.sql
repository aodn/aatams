select count(*), species.common_name, species.scientific_name, species.spcode from raw_detection
join detection_surgery on raw_detection.id = detection_surgery.id
join surgery on detection_surgery.surgery_id = surgery.id
join animal_release on surgery.release_id = animal_release.id
join animal on animal_release.animal_id = animal.id
join species on animal.species_id = species.id
where raw_detection.class = 'au.org.emii.aatams.detection.ValidDetection'
group by species.spcode, species.scientific_name, species.common_name
order by count (*) desc;

--select count(*) as "detections by species" from valid_detection
--join detection_surgery on valid_detection.id = detection_surgery.detection_id
--join surgery on detection_surgery.surgery_id = surgery.id
--join animal_release on surgery.release_id = animal_release.id
--join animal on animal_release.animal_id = animal.id
--join species on animal.species_id = species.id
--order by count (*) desc;
