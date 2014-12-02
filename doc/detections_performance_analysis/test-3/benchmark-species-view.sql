
set role to aatams;
set role to aatams;

-- Low selectivity filtering (large number of rows returned for each filter)

-- Test Case 1 - 2716283 rows limited to first 100

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Heron Island'
limit 100;

-- Test Case 2 - 3913743 rows limited to first 100

explain analyze select * from species_detection_extract_test_view
where spcode = '37018030'
limit 100;

-- Test Case 3 - 17098775 rows limited to first 100

explain analyze select * from detection_extract_test_view
where timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 4 -  2716283 ^ 3913743 -> 1420919 rows limited to first 100

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37018030'
limit 100;

-- Test Case 5 - 2716283 ^ 17098775 -> 902906 rows limited to first 100

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Heron Island'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 6 - 3913743 ^ 17098775 -> 2233756 rows limited to first 100

explain analyze select * from species_detection_extract_test_view
where spcode = '37018030'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 7 - 2716283 ^ 3913743 ^ 17098775 -> 690003 rows limited to first 100

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37018030'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;
 
-- High selectivity filtering (small number of rows returned for each filter)

-- Test Case 8 - 5909 rows

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
limit 100;

-- Test Case 9 - 2153 rows
set geqo_threshold to 12;

explain analyze select * from species_detection_extract_test_view
where spcode = '37018003'
limit 100;

-- Test Case 10 - 1065 rows

explain analyze select * from detection_extract_test_view
  where timestamp between '2014-07-30 12:00:00+00' and '2014-07-30 18:00:00+00'
limit 100;

-- Test Case 11 - 5909 ^ 2153 -> 609 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018003'
limit 100;

-- Test Case 12 - 5909 ^ 1065 -> 55 rows

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and timestamp between '2014-07-30 12:00:00+00' and '2014-07-30 18:00:00+00'
limit 100;

-- Test Case 13 -  2153 ^ 1065 -> 55 rows

explain analyze select * from species_detection_extract_test_view
where spcode = '37018003'
  and timestamp between '2014-07-30 12:00:00+00' and '2014-07-30 18:00:00+00'
limit 100;

-- Test Case 14 -  5909 ^ 2153 ^ 1065 -> 55 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018003'
  and timestamp between '2014-07-30 12:00:00+00' and '2014-07-30 18:00:00+00'
limit 100;

-- Mixed selectivity filtering

-- Test Case 15 - 5909 ^ 833188 -> 184 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018021'
limit 100;

-- Test Case 16 - 5909 ^ 17098775 -> 3889 rows

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 17 - 2716283 ^ 464 -> 448 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37346004'
limit 100;

-- Test Case 18 - 2716283 ^ 2005 -> 4 rows

explain analyze select * from detection_extract_test_view
where project = 'AATAMS Heron Island'
  and timestamp between '2013-06-08 00:00:00+00' and '2013-06-08 01:00:00+00'
limit 100;

-- Test Case 19 - 464 ^ 17098775 -> 6 rows

explain analyze select * from species_detection_extract_test_view
where spcode = '37346004'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 20 - 833188 ^ 5489 -> 210 rows

explain analyze select * from species_detection_extract_test_view
where spcode = '37018021'
  and timestamp between '2013-09-24 00:00:00+00' and '2013-09-24 04:00:00+00'
limit 100;

-- Test Case 21 - 5909 ^ 2153 ^ 17098775 -> 284 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018003'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 22 - 5909 ^ 833188 ^ 5489 -> 13 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018021'
  and timestamp between '2013-09-24 00:00:00+00' and '2013-09-24 04:00:00+00'
limit 100;

-- Test Case 23 - 5909 ^ 833188 ^ 17098775 -> 54 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Coffs Harbour line'
  and spcode = '37018021'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 24 - 2716283 ^ 464 ^ 3129 -> 2 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37346004'
  and timestamp between '2013-04-02 05:00:00+00' and '2013-04-02 07:00:00+00'
limit 100;

-- Test Case 25 - 2716283 ^ 464 ^ 17098775 -> 4 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37346004'
  and timestamp between '2012-03-01' and '2013-09-01'
limit 100;

-- Test Case 26 - 2716283 ^ 3913743 ^ 3129 -> 52 rows

explain analyze select * from species_detection_extract_test_view
where project = 'AATAMS Heron Island'
  and spcode = '37018030'
  and timestamp between '2013-04-02 05:00:00+00' and '2013-04-02 07:00:00+00'
limit 100;

\q
