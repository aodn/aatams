*Note on styles used in this document: concepts or terms with associated classes/tables are styled `like this`, at least the first time they are used.*


# Domain Concepts

At the highest level, there are three things that one needs to know about: `receivers`, `tags` and `detections`.  A tagged animal being in the vicinity of a receiver will result in one or more `detections`. 

## Receivers
A receiver is `deployed` underwater at a `station`.  After some time (months or years), it is `recovered`.

## Tags
A tag is attached to an animal (known as `surgery`) and `released`.  A single physical tag may have several `sensors` on board (e.g. to sense temperature, pressure and acceleration).  Tag lifetime varies, but it is of the order of a few years.

## Detections
As mentioned above, receivers detect tags when they are nearby.


# Security
Most other domain classes which exist are to support security, and who can see what.  The most important security restrictions are:

* a `person` must be logged in to create or edit data (based around what `projects` and `organistations` they belong to);
* some release data is embargoed, meaning that it can only be seen by people with the appropriate permissions;
* otherwise, most other things are visible even to unauthenticated users.



