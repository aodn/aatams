# AATAMS Database Assumptions
Because of the nature of the AATAMS data, and the way in which the community operates, there are a number of assumptions made by the AATAMS Database web-app. This is an attempt to document those assumptions so that the information provided by the web-app and the AATAMS Monthly Reports can be interpreted correctly.

## Detections
*n.b. Recent work on [GitHub issue #208](https://github.com/aodn/aatams/issues/208) has changed some of the assumptions around how detections work. The following referes to the latest state of the code.*

The database understands that a transmitter may be attached to more than one animal in its lifetime. Detections are associated with only the most recent surgery for that tag. Detections with no known surgeries, or which occur before all known surgeries, are not associated with any surgery.

The correctness of the detections data is conditional on the recorded **surgery information** being **complete and accurate**. Some information about detections (e.g. the species of the animal) is determined from surgery information. So if new surgeries are entered, or if an existing surgery is updated, it may affect the detection information in the web-app or monthly reports.

#### Reports
The recent change will have a once-off major effect on the AATAMS Monthly Reports. The number of detections will go down by around 1.2 million from the previous months. Once this correction has happened the monthly numbers should be consistent.