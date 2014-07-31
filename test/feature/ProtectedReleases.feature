Feature: protected releases
    As a tag data provider, I would like to limit visibility of detections of my tags to authorised users only.

# Version: 0.0.2


Scenario Outline: project protection configuration
    Given I have navigated to the "Create/Edit Project" screen
    Given I am <auth level>   # logged in as a sys admin
    Then a check box labelled "Protected" will be <visibility> # visible and enabled

    Examples:

        | auth level                  | visibility  |
        | sys-admin user              | visible     |
        | authenticated, project user | not visible |


Scenario Outline: protection notification
    Given I have navigated to the "Create/Edit/Show" <screen> screen
    And the project is <protection level>
    Then a notification <screen> "is protected" will be <visibility>

    Examples:

        | screen  | protection level | visibility  |
        | Project | protected        | visible     |
        | Project | non protected    | not visible |
        | Release | protected        | visible     |
        | Release | non protected    | not visible |


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


Scenario Outline: detection and release visibility
    Given there is a protected release with associated detections
    And I am <auth level>   # I am not logged in
    And the current date is <relative date> the protected release's embargo expiry date  # before or equal to
    Then the detections, releases and associated entities shall be <visibility> # not visible or downloadable

    Examples:

        | auth level                      | relative date | visibility  |
        |                                 |               |             |
        | unathenticated                  | after         | visible     |
        | unathenticated                  | before        | not visible |
        | authenticated, non project user | before        | not visible |
        | authenticated, project user     | before        | visible     |


Scenario Outline: filtering
    Given I have navigated to the "Tag Detection List" screen
    And I am <auth level>
    And there is a <protection level> release with species "x" and associated detections
    And I have <filter set> the "species" filter to "x"
    Then the detections should be <visibility> in the list

    Examples:

        | auth level                      | protection level | filter set | visibility  |
        |                                 |                  |            |             |
        | unathenticated                  | unembargoed      | set        | visible     |
        | unathenticated                  | embargoed        | not set    | visible     |
        | unathenticated                  | embargoed        | set        | not visible |
        | unathenticated                  | protected        | not set    | not visible |
        | unathenticated                  | protected        | set        | not visible |
        |                                 |                  |            |             |
        | authenticated, non project user | unembargoed      | set        | visible     |
        | authenticated, non project user | embargoed        | not set    | visible     |
        | authenticated, non project user | embargoed        | set        | not visible |
        | authenticated, non project user | protected        | not set    | not visible |
        | authenticated, non project user | protected        | set        | not visible |
        |                                 |                  |            |             |
        | authenticated, project user     | unembargoed      | set        | visible     |
        | authenticated, project user     | embargoed        | not set    | visible     |
        | authenticated, project user     | embargoed        | set        | visible     |
        | authenticated, project user     | protected        | not set    | visible     |
        | authenticated, project user     | protected        | set        | visible     |
        |                                 |                  |            |             |
        | sys-admin user                  | unembargoed      | set        | visible     |
        | sys-admin user                  | embargoed        | not set    | visible     |
        | sys-admin user                  | embargoed        | set        | visible     |
        | sys-admin user                  | protected        | not set    | visible     |
        | sys-admin user                  | protected        | set        | visible     |
