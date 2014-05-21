# Summary
A very brief outline of the plans to directly integrate VUE and the AATAMS DB.

Note that there is provisional agreement between Vemco and AATAMS representatives to go with option 2, but both options discussed have been included for completeness.


# Conversion
### Option 1 - conversion to XML done by AATAMS
* VRLs only uploaded to the AATAMS webserver by VUE
* VRLs converted to XML and then parsed by AATAMS.  This implies that IMOS would need to install and maintain an up to date version of the VRL -> XML conversion software (supplied by Vemco)

### Option 2 - conversion to XML done by VUE
* VRLs converted to XML by VUE
* XML uploaded to the AATAMS webserver by VUE
* VRLs also uploaded to AATAMS - won't be read but will be stored as a backup in case it's found at a later date something is missing from the XML format.


# Upload
In both scenarios above, *something* would have to be uploaded to the AATAMS system by VUE.

Probably the easiest way to do this would be to open up a web service in the AATAMS app to which the VUE software can [HTTP POST](http://en.wikipedia.org/wiki/POST_(HTTP\)) its payload.

Things to consider are:

* the client (i.e. VUE) will have to authenticate with a username/password
* the HTTP POST will need to include attached data (i.e. the VRL file)
* what upper limit do we want to impose on filesize (10s of MBs is probably ok, 100s of MBs - probably too much - see [Stats](#Stats) below). Significant compression could probably be achieved given the nature of the XML, so this could alleviate this somewhat if it's an issue

### Example request


An example of what a request for "option 2" above might look like, using [curl](http://curl.haxx.se/):

```
$ curl --data "username=jbloggs&password=asdf" \
       --data @converteddata.xml \
       --header "Content-Type: application/xml" \
       -F "vrl=@xyz.vrl;type=application/octet-stream" \
       https://aatams.emii.org.au/aatams/receiverData/upload
```

The response to the above request would include a standard HTTP response code, e.g. 200 to indicate success, 500 to indicate an internal server error etc.

### Stats

To give some idea of what sort of upload sizes we might be dealing with, here are some stats about the existing *CSV* uploads (which are likely to be smaller in general than the XML, but roughly the same order of magnitude):

* ~1300 CSV uploads in total
* largest are around ~200MB, but...
* ~85% are 10MB or smaller
* ~80% are 5MB or smaller

Given these basic stats, it is quite likely feasible to use HTTP POST for upload.