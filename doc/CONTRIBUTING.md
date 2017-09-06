# Development Environment

1. Install git and curl
1. Clone AATAMS repository
    `$ git clone https://github.com/aodn/aatams.git`
1. Switch to AATAMS project directory
    `$ cd aatams`
1. Install [SDKMAN](http://sdkman.io/install.html)
1. Install Grails
    `$ sdk use grails 1.3.7`
1. Install Java
    `$ sdk use java 6u93-zulu`
1. Install [Postgres](http://postgresguide.com/setup/install.html) and [PostGIS](http://postgis.net/install/)
1. Setup development database
    `$ sudo -u postgres ./init_db.sh`
1. Running app for local development 
    `$ grails clean`
    `$ grails run-app`
1. Running test 
    `$ grails clean`
    `$ sudo -u postgres ./init_test_db.sh`
    `$ grails test-app --echoOut`