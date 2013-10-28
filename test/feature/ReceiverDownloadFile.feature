Feature: export visibility
    As a shark I would like the receiver export list to be viewed only by the uploader so that I don't get shot.


Scenario: receiver export list view as unathenticated user
    Given I am not logged in
    And someone else has uploaded a receiver export
    Then I should not be able to see any receiver exports

Scenario: receiver export list view as normal user
    Given I am logged in as a normal user
    And I have uploaded a receiver export
    And someone else has uploaded a receiver export
    Then the receiver export list view should show only my receiver export

Scenario: receiver export list view as sys admin
    Given I am logged in as a sys admin
    And someone else has uploaded a receiver export
    Then the receiver export list view should show the other person's export



Scenario: receiver export show view of own export as normal user
    Given I am logged in as a normal user
    And I have uploaded a receiver export
    Then I should be able to navigate to the show view of that receiver export
    And I should be able to download the associated export file from the show view

Scenario: receiver export show view of someone else's export as sys admin
    Given I am logged in as a sys admin
    And someone else has uploaded a receiver export
    Then I should be able to navigate to the show view of that receiver export
    And I should be able to download the associated export file from the show view



Scenario: direct navigation to receiver export show view as unauthenticated user
    Given I am not logged in
    And I have uploaded a receiver export
    And I attempt to go directly to the show view of my receiver export
    Then I should be redirected to the sign in screen
    And subsequently be redirected to the show view

Scenario: direct navigation to receiver export show view of someone else's export
    Given I am logged in as a normal user
    And someone else has uploaded a receiver export
    And I attempt to go directly to the show view of that receiver export
    Then I should see 'not authorized' in the browser  # I'm not sure if these are the exact words - there's already a precedent though.

Scenario: direct navigation to receiver export show view of someone else's export as sysadmin
    Given I am logged in as a sys admin
    And someone else has uploaded a receiver export
    And I go directly to the show view of that receiver export
    Then I should be able to see it
