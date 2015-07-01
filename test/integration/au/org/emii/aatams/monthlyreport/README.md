# AATAMS Monthly Reports

The AATAMS monthly reports co-exist with the web-app/DB, but are not part of it, as such.  However, the reports' depend on the DB structure to work properly.

Without some form of automated testing, it's impossible to guarantee that a change to the DB schema will not break the report SQL - the SQL used in the reports is always playing "catch-up" to the changes made in the web-app/DB

The suite of tests in this directory aims to rectify that situation.

Add a new `.sql` file to this directory, and it will automatically be tested.

######Note: these tests *do not* guarantee the *correctness* of the reports, only that the SQL is valid against the DB.*

