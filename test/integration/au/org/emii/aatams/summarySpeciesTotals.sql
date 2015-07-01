WITH total_species AS (
SELECT COUNT() AS t,
'total no tagged species' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_species_public AS(
SELECT COUNT() AS t,
'no tagged species for which all animals are public' AS statistics_type
FROM aatams_acoustic_species_data_summary_view
WHERE no_embargo = 0),
total_species_embargo AS (
SELECT COUNT() AS t,
'no tagged species for which some animals are currently under embargo' AS statistics_type
FROM aatams_acoustic_species_data_summary_view
WHERE no_embargo != 0),
total_species_detections AS (
SELECT COUNT() AS t,
'no tagged species with detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view
WHERE total_no_detections != 0),
total_species_public_detections AS (
SELECT COUNT() AS t,
'no tagged species with public detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view
WHERE no_detections_public != 0),
total_species_embargo_detections AS (
SELECT COUNT() AS t,
'no tagged species with embargoed detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view
WHERE no_detections_public = 0 AND total_no_detections != 0),
total_animals AS (
SELECT SUM (no_releases) AS t,
'total no tagged animals' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_animals_public AS (
SELECT SUM (no_releases) - SUM(no_embargo) AS t,
'no tagged animals that are public' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_animals_embargo AS (
SELECT SUM(no_embargo) AS t,
'no tagged animals currently under embargo' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_animals_detections AS (
SELECT SUM (no_releases_with_detections) AS t,
'no tagged animals with detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_animals_public_detections AS (
SELECT SUM (no_public_releases_with_detections) AS t,
'no tagged animals with public detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_animals_embargo_detections AS (
SELECT SUM(no_releases_with_detections) - SUM(no_public_releases_with_detections) AS t,
'no tagged animals with embargoed detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_detections AS (
SELECT SUM (total_no_detections) AS t,
'no detections' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_detections_public AS (
SELECT SUM (no_detections_public) AS t,
'no detections that are public' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
total_detections_embargo AS (
SELECT SUM (total_no_detections) - SUM (no_detections_public) AS t,
'no detections that are currently under embargo' AS statistics_type
FROM aatams_acoustic_species_data_summary_view),
tag_ids AS (
SELECT COUNT(DISTINCT transmitter_id) AS t,
'no unique tag ids detected' AS statistics_type
FROM valid_detection),
tag_aatams_knows AS (
SELECT COUNT() AS t,
'tag aatams knows about' AS statistics_type
FROM device
WHERE class = 'au.org.emii.aatams.Tag'),
detected_tags_aatams_knows AS (
SELECT COUNT(DISTINCT sensor.transmitter_id) AS t,
'no detected tags aatams knows about' AS statistics_type
FROM valid_detection
JOIN sensor ON valid_detection.transmitter_id = sensor.transmitter_id),
tags_by_species AS (
SELECT COUNT(DISTINCT s.tag_id) AS t,
'tags detected by species' AS statistics_type
FROM valid_detection vd
LEFT JOIN sensor on vd.transmitter_id = sensor.transmitter_id
LEFT JOIN device d on sensor.tag_id = d.id
JOIN surgery s ON s.tag_id = d.id),
embargo_1 AS (
SELECT COUNT() AS t,
'Total number of tags embargoed for less than or equal to a year' AS statistics_type
FROM animal_release ar
WHERE ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) <= 1 AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) IS NOT NULL),
embargo_2 AS (
SELECT COUNT() AS t,
'Total number of tags embargoed for more than a year, but less than or equal to 2 years' AS statistics_type
FROM animal_release ar
WHERE ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) > 1 AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) <= 2
AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) IS NOT NULL),
embargo_3 AS (
SELECT COUNT() AS t,
'Total number of tags embargoed for more than two years, but less than or equal to 3 years' AS statistics_type
FROM animal_release ar
WHERE ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) > 2 AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) <= 3
AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) IS NOT NULL),
embargo_3_more AS (
SELECT COUNT(*) AS t,
'Total number of tags embargoed for more than three years' AS statistics_type
FROM animal_release ar
WHERE ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) > 3 AND ROUND((EXTRACT (day FROM embargo_date - releasedatetime_timestamp)/365.25)::numeric,1) IS NOT NULL)
SELECT 'Species' AS type,
s.t AS total,
sp.t AS total_public,
se.t AS total_embargo,
sd.t AS detections_total,
spd.t AS detections_public,
sed.t AS detections_embargo,
NULL::bigint AS other_1,
NULL::bigint AS other_2
FROM total_species s, total_species_public sp,total_species_embargo se, total_species_detections sd, total_species_public_detections spd,total_species_embargo_detections sed
UNION ALL
SELECT 'Animals' AS type,
a.t AS total,
ap.t AS total_public,
ae.t AS total_embargo,
ad.t AS detections_total,
apd.t AS detections_public,
aed.t AS detections_embargo,
NULL AS other_1,
NULL AS other_2
FROM total_animals a, total_animals_public ap,total_animals_embargo ae, total_animals_detections ad, total_animals_public_detections apd,total_animals_embargo_detections aed
UNION ALL
SELECT 'Detections' AS type,
NULL AS total,
NULL AS total_public,
NULL AS total_embargo,
d.t AS detections_total,
dp.t AS detections_public,
de.t AS detections_embargo,
NULL AS other_1,
NULL AS other_2
FROM total_detections d, total_detections_public dp, total_detections_embargo de

UNION ALL
SELECT 'Other stats' AS type,
ti.t AS total,
ta.t AS total_public,
dta.t AS total_embargo,
ts.t AS detections_total,
e1.t AS detections_public,
e2.t AS detections_embargo,
e3.t AS other_1,
e3m.t AS other_2
FROM tag_ids ti, tag_aatams_knows ta, detected_tags_aatams_knows dta, tags_by_species ts, embargo_1 e1, embargo_2 e2, embargo_3 e3, embargo_3_more e3m;
