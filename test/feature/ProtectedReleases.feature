Feature: protected releases
    As a tag data provider, I would like to limit visibility of detections of my tags to authorised users only.

# Version: 0.0.2

#
# Configuring protected releases by project.
#
Scenario: project create/edit view as sys admin user
    Given I am logged in as a sys admin
    And I have navigated to the "Create Project" screen
    Then a check box labelled "Protected" will be visible and enabled

Scenario: project create/edit view as authenticated, non sys admin user, protected project
    Given I am logged in as a non sys admin
    And I have navigated to the "Create Project" screen
    And the project is protected
    Then a notification "Project is protected" will be visible
    And a check box labelled "Protected" will not be visible

Scenario: project create/edit view as authenticated, non sys admin user, non protected project
    Given I am logged in as a non sys admin
    And I have navigated to the "Create Project" screen
    And the project is not protected
    Then a notification "Project is protected" will not be visible
    And a check box labelled "Protected" will not be visible

Scenario: project show view as authenticated, non sys admin user
    Given I am logged in as a non sys admin
    And there is a protected project
    And I have navigated to the "Show Project" screen for the project
    Then a notification "Project is protected" will be visible

Scenario: release create/edit view as authenticated user, protected project, embargoed release
    Given I am logged in as a non sys admin
    And there is a protected project
    And I have navigated to the "Create Release" screen
    And I have set an embargo period
    Then a notification "Release is protected" will be visible

Scenario: release create/edit view as authenticated user, protected project, non embargoed release
    Given I am logged in as a non sys admin
    And there is a protected project
    And I have navigated to the "Create Release" screen
    And I have not set an embargo period
    Then a notification "Release is protected" will not be visible

Scenario: release create/edit view as authenticated user, non protected project
    Given I am logged in as a non sys admin
    And there is a non protected project
    And I have navigated to the "Create Release" screen
    Then a notification "Project is protected" will not be visible


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
# Notes:
# 
# - "Visibility" in this context means "list" and "show" views, as well as downloads (i.e. CSV and KMZ exports).
# - A release is considered to be protected if it's owning project is protected and it is currently under embargo.
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

#
# Filtering
#

Scenario: non embargoed release, filtering as unauthenticated user
    Given I am not logged in
    And there is an unembargoed release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have set the "species" filter to "x"
    Then the detections should be visible in the list


Scenario: embargoed release, no filtering as unauthenticated user
    Given I am not logged in
    And there is an embargoed release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have not set the "species" filter
    Then the detections should be visible in the list

Scenario: embargoed release, filtering as unauthenticated user
    Given I am not logged in
    And there is an embargoed release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have set the "species" filter to "x"
    Then the detections should not be visible in the list

Scenario: protected release, no filtering as unauthenticated user
    Given I am not logged in
    And there is a protected release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have not set the "species" filter to "x"
    Then the detections should not be visible in the list

Scenario: protected release, filtering as unauthenticated user
    Given I am not logged in
    And there is a protected release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have set the "species" filter to "x"
    Then the detections should not be visible in the list

Scenario: protected release, filtering as authenticated user
    Given I am not logged in
    And there is a protected release with species "x" and associated detections
    And I have navigated to the "Tag Detection List" screen
    And I have set the "species" filter to "x"
    Then the detections should not be visible in the list

# Authenticated, non-project user - as above
# Authenticated, project user - as above, except detections will be visible in all cases
# Sys-admin user - as above, except detections will be visible in all cases
