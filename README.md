[![Build Status](https://travis-ci.org/aodn/aatams.png?branch=master)](https://travis-ci.org/aodn/aatams)

## Overview
Animal Tracking, formerly Australian Animal Tracking and Monitoring System (AATAMS), is one of eleven facilities of the Integrated Marine Observing System (IMOS). Animal Tracking represents the higher biological monitoring of the marine environment for the IMOS program.

This repository is for the web application which supports the management of Animal Tracking data.

The repository name remains as AATAMS due to the complexity of changes required in renaming the repository and associated links.

## Application Features

### Upload and Storage
The web app enables the *upload* and *storage* of Animal Tracking data, which can be broadly categorised as:

* tag detections and receiver eventsa
  * downloaded from receivers and uploaded (as CSV files) to the app
  * information about the presence of a particular tag within the vicinity of a particular receiver and receiver statistics/diagnostic information, respectively
* metadata
  * entered manually through a web UI
  * information about people, projects and organisations
  * information about receivers and when and where they are deployed
  * infromation about tags and when, where and to what species they are deployed

### Access
The web application also gives the following *access* to the data:

* a web UI, with list and individual item views of all the data
* searching and filtering of data
* selected CSV and PDF file downloads
* PDF summary reports

### Security
At all times, a number of security rules are enforced, restricting who can *upload* data, and what people can *access*:

* only those with write access can upload data to the database
* certain data (namely *tags, releases and detections*) can be *embargoed* or *protected* (a more restrictive rule), in which case it can only be seen by those with the correct authorisation level (based on project membership)

### Notifications
Email notifications are sent to users in the following cases:

* visibility of user data has changed or is soon about to change
* new data has been uploaded (by another user) which is related to a user's own data


## High Level Domain Concepts and Design
See [doc/DESIGN.md](doc/DESIGN.md).

## Development

### Development Environment
See [doc/CONTRIBUTING.md](doc/CONTRIBUTING.md).

### Restoring the AATAMS database
See [doc/BACKUP_AND_RESTORE.md](doc/BACKUP_AND_RESTORE.md).
