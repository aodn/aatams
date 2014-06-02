Feature: protected releases
    As a tag data provider, I would like to limit visibility of detections of my tags to authorised users only.

#
# Configuring protected releases.
#
Scenario: release create/edit view as sys admin user, embargo period set
    Given I am logged in as a sys admin
    And I have navigated to the "Create Tag Release" screen
    And the embargo period is set to something other than "No embargo"
    Then a check box labelled "Protected" will be visible and enabled

Scenario: release create/edit view as sys admin user, embargo period not set
    Given I am logged in as a sys admin
    And I have navigated to the "Create Tag Release" screen
    And the embargo period is set to "No embargo"
    Then a check box labelled "Protected" will be visible but not enabled

Scenario: release create/edit view as authenticated, non sys admin user
    Given I am logged in as a non sys admin
    And I have navigated to the "Create Tag Release" screen
    Then no check box labelled "Protected" will be visible

Scenario: release show view as authenticated, non sys admin user
    Given I am logged in as a non sys admin
    And there is a protected release
    And I have navigated to the "Show Tag Release" screen for the release
    Then a check box labelled "Protected" will be visible and checked, but not enabled

#
# Visibility of protected releases and detections, including the following associated entities (list with their
# associated base URL):
#
# animal -              http://aatams-rc.emii.org.au/aatams/animal
# detection -           http://aatams-rc.emii.org.au/aatams/detection
# detectionSurgery -    http://aatams-rc.emii.org.au/aatams/detectionSurgery
# sensor -              http://aatams-rc.emii.org.au/aatams/sensor
# surgery -             http://aatams-rc.emii.org.au/aatams/surgery
# release -             http://aatams-rc.emii.org.au/aatams/animalRelease
# tag -                 http://aatams-rc.emii.org.au/aatams/tag
#
# "Visibility" in this context means "list" and "show" views, as well as downloads (i.e. CSV and KMZ exports).
#

Scenario: detection, release visibility as unauthenticated user during embargo period
    Given I am not logged in
    And there is a protected release with associated detections
    And the current date is before or equal to the protected release's embargo expiry date
    Then the detections, releases and associated entities shall not be visible or downloadable

Scenario: detection, release visibility as authenticated, non-project user during embargo period
    Given I am logged in
    And there is a protected release with associated detections
    And the current date is before the protected release's embargo expiry date
    And I am not a member of the project associated with the protected release
    Then the detections, releases and associated entities shall not be visible or downloadable

Scenario: detection, release visibility as authenticated, project user during embargo period
    Given I am logged in
    And there is a protected release with associated detections
    And the current date is before the protected release's embargo expiry date
    And I am a member of the project associated with the protected release
    Then the detections, releases and associated entities shall be visible and downloadable

Scenario: detection, release visibility as unauthenticated user after embargo period
    Given I am not logged in
    And there is a protected release with associated detections
    And the current date is after the protected release's embargo expiry date
    Then the detections, releases and associated entities shall be visible and downloadable
