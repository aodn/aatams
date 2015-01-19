## AATAMS Detection Extract Performance Analysis

### Background

The tag detection list page (https://aatams.emii.org.au/aatams/detection/list) is currently supported by a materialised view which is problematic.  

This page looks at whether it is possible to use a simple view or views on the source tables instead of the materialised view to support the tag detection list page and in particular whether it is possible to do so with acceptable performance.

### Findings

* Need to have at least enough memory to be able to use indexes appropriately when needed
* Need to set postgres tuning options to reflect available memory so that indexes are used when appropriate
* Need to use views appropriate to the filtering options selected to help postgres generate efficient query plans

With the above the postgres query planner seems to be able to generate query plans that perform acceptably using views on the source table

### Qualifications

Performs well enough for test cases selected, but may not perform well in other cases

### Method

* Prepared benchmarking test cases to be used to compare different tuning and sql options
* Ran benchmarking tests three times starting from a cold database starting from the default settings for the test database and the backing view used for the current materialised view
* Examined query plans used by postgres for poorly performing test cases to determine possible changes that could be made to improve performance
* Repeated benchmarking tests for changes and examined query plans as above until performance issues appeared to be resolved

### Benchmark Test Cases

The benchmarking test cases were created as follows:

* Used species, project and timestamp as representative filtering possibilities for tags, detections and receivers
* Collected statistics on records returned for each species, project and time range and combinations of these options
* Picked different combinations of very selective to poorly selective filtering options for the above to cover different filtering possibilities currently available through the application

Test Cases

Test Case | Project (low) | Project (high) | Species (low) | Species (high) | Timestamp (low) | Timestamp (high)
 :---:| :---: | :---: | :---:| :---: | :---: | :---:| :---: | :---:
1 | | x 
2 | | | | x
3 | | | | | | x
4 | | x | | x
5 | | x | | | | x
6 | | | | x | | x
7 | | x | | x | | x
8 | x 
9 | | | x
10 | | | | | x
11 | x | | x
12 | x | | | | x
13 | | | x | | x
14 | x | | x | | x
15 | x | | | x
16 | x | | | | | x
17 | | x | x
18 | | x | | | x
19 | | | x | | |  x
20 | | | | x | x
21 | x | | x | | | x
22 | x | | | x | x
23 | x | | | x | | x
24 | | x | x | | x
25 | | x | x | | | x
26 | | x | | x | x

### Not Examined

Sorting of results for pagination

### Initial Test

* Shared memory - 2 GB
* Effective cache size - 4 GB
* [Current view](detections_performance_analysis/initial-test/detection_view_initial.sql)
* [Test script (limited to first 100 records)](detections_performance_analysis/initial-test/benchmark-initial.sql)

Results 

 Test Case | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26
---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---
[Run 1 (ms)](detections_performance_analysis/initial-test/benchmark-initial-1.out) | 1854.529|114.543|21.363|63.925|15.469|3682.613|3092.964|4131.325|40657.269|26.551|1859.093|11.636|37.423|11.077|12902.653|3086.795|35502.923|7.392|68849.801|102.697|9095.446|13.233|13621.759|16.727|19923.882|16.508
[Run 2 (ms)](detections_performance_analysis/initial-test/benchmark-initial-2.out) |4091.600|100.570|21.094|62.373|68.620|3538.775|3101.057|4667.172|89.473|26.481|61.324|11.497|37.686|11.130|6716.332|3126.929|35438.400|9.795|68361.556|102.728|9096.254|12.301|13611.502|16.571|19917.722|16.567
[Run 3 (ms)](detections_performance_analysis/initial-test/benchmark-initial-3.out) | 4086.814|100.491|20.981|62.187|68.120|3625.503|3101.935|4683.903|90.713|26.336|61.959|11.456|37.910|11.158|6731.961|3121.575|35419.077|6.694|68216.643|102.481|9038.993|12.238|13601.407|16.478|19875.372|16.474

### Adjusting memory settings

* Shared memory - 6 GB
* Effective cache size - 12 GB
* [Current view](detections_performance_analysis/initial-test/detection_view_initial.sql)
* [Test script (limited to first 100 records)](detections_performance_analysis/initial-test/benchmark-initial.sql)

Results 

 Test Case | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26
---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---
[Run 1 (ms)](detections_performance_analysis/test-2/benchmark-test-2-1.out) |13.000|15968.441|11.431|67.177|7.877|33.823|67.091|5.175|92036.639|11.442|71.765|4.207|38.638|3.842|73.616|4.914|10383.465|3.475|454371.447|71.164|75.783|5.619|83.421|7.419|4888.694|6.434
[Run 2 (ms)](detections_performance_analysis/test-2/benchmark-test-2-2.out) |11.515|2680.180|8.212|66.436|5.739|1271.953|66.104|4.856|14565.581|11.019|71.008|3.910|38.569|3.832|73.602|4.876|9819.816|2.571|452005.160|69.312|74.856|4.917|83.162|5.975|4877.775|6.349
[Run 3 (ms)](detections_performance_analysis/test-2/benchmark-test-2-3.out) |11.474|3854.593|8.132|66.476|5.702|335.179|66.106|4.836|65487.622|11.047|70.784|3.865|38.545|3.800|73.540|4.777|9758.223|2.484|451976.305|69.229|74.878|4.911|83.782|5.952|4884.116|6.396

### Using species specific view when filtering on species - option #1

* Shared memory - 6 GB
* Effective cache size - 12 GB
* [Specific view for species filtering - postgres decides optimal plan](detections_performance_analysis/test-3/species_detection_extract_view_postgres_decides.sql)
* [Testscript (limited to first 100 records)](detections_performance_analysis/test-3/benchmark-species-view.sql)

Results 

 Test Case | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26
---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---
[Run 1 (ms)](detections_performance_analysis/test-3/benchmark-test-3-1.out) |12.945|12776.084|11.272|47.527|7.849|33.738|46.731|5.185|66638.176|11.249|51.512|4.163|38.321|3.812|53.273|4.861|890.724|3.515|387230.829|84.849|54.838|5.492|59.477|7.388|4864.612|6.251
[Run 2 (ms)](detections_performance_analysis/test-3/benchmark-test-3-2.out) |11.506|1983.511|8.080|46.747|5.690|1207.055|46.718|4.848|10505.051|10.929|50.557|3.868|38.500|3.845|53.257|4.768|689.640|2.557|386897.255|83.222|54.475|4.866|59.486|5.851|4478.399|6.293
[Run 3 (ms)](detections_performance_analysis/test-3/benchmark-test-3-3.out) |11.528|2555.879|8.049|46.744|5.690|319.013|46.662|4.851|49058.889|10.942|50.492|3.807|38.489|3.845|52.988|4.744|678.122|2.458|386844.799|83.166|54.510|4.839|59.494|5.879|4475.428|6.300

### Using species specific view when filtering on species - option #2

* Shared memory - 6 GB
* Effective cache size - 12 GB
* [Specific view for species filtering - force use of species filter first](detections_performance_analysis/test-4/species_detection_extract_view_force_use_of_species_filter_first.sql)
* [Testscript (limited to first 100 records)](detections_performance_analysis/test-3/benchmark-species-view.sql)

Results 

 Test Case | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26
---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---
[Run 1 (ms)](detections_performance_analysis/test-3/benchmark-test-4-1.out) | 12.962|70.456|11.791|4522.402|7.893|87.126|487.419|5.218|50.248|11.841|38.281|4.263|34.176|33.042|1408.486|5.172|35.241|3.419|74.685|277.533|36.287|257.074|1358.816|30.415|33.407|43.455
[Run 2 (ms)](detections_performance_analysis/test-3/benchmark-test-4-2.out) | 11.434|60.895|8.388|3807.266|5.718|86.569|487.311|4.964|49.638|11.656|36.919|3.961|34.185|33.029|1170.897|4.876|34.451|2.706|56.884|274.707|36.186|254.979|1335.055|29.521|33.479|43.352
[Run 3 (ms)](detections_performance_analysis/test-3/benchmark-test-4-3.out) | 11.590|61.266|8.429|3820.320|5.752|86.571|486.193|4.958|49.726|11.901|37.102|3.996|34.160|33.089|1172.736|5.005|34.713|2.723|57.078|275.380|36.330|255.772|1344.827|29.510|33.470|43.447

### Downloading/Counts

* Shared memory - 6 GB
* Effective cache size - 12 GB
* [Specific view for species filtering - force use of species filter first](detections_performance_analysis/test-4/species_detection_extract_view_force_use_of_species_filter_first.sql)
* [Test script (limited to first 300000 records)](detections_performance_analysis/test-5/benchmark-species-view-limit-300000.sql)

 Test Case | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26
---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---
[Run 1 (ms)](detections_performance_analysis/test-3/benchmark-test-5-1.out) | 3543.561|3233.892|6566.609|7781.741|4162.421|3639.483|9361.719|133.952|65.220|45.675|45.906|3.892|34.235|33.251|1665.852|105.592|42.492|3.330|41.723|276.870|40.264|259.238|1350.204|30.432|32.977|43.520
[Run 2 (ms)](detections_performance_analysis/test-3/benchmark-test-5-2.out) | 3226.986|3069.617|6299.842|7098.055|3975.597|3610.962|9259.583|127.217|63.015|44.799|45.176|3.825|33.976|32.960|1390.171|104.855|41.497|2.593|41.455|271.870|40.251|254.433|1336.760|29.313|33.112|43.149
[Run 3 (ms)](detections_performance_analysis/test-3/benchmark-test-5-3.out) |3239.227|3081.405|6309.660|7126.069|3977.496|3620.141|9286.648|127.572|63.261|44.777|45.390|3.839|34.149|33.079|1395.881|105.256|41.660|2.593|41.649|272.131|40.449|255.180|1343.115|29.476|33.260|43.264



