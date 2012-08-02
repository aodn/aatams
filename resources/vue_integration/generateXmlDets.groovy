#!/usr/bin/env groovy

import java.text.SimpleDateFormat

def numDets = 800000

def xmlFile = new File('generatedExample.xml')
def xmlWriter = xmlFile.newWriter()

def sqlFile = new File('generatedExample.sql')
def sqlWriter = sqlFile.newWriter()


def xmlHeader = '''<?xml version="1.0" encoding="UTF-8"?>

<vueOffload xmlns="http://vemco.com/vue"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://vemco.com/vue vue.xsd">

    <deviceModelList>
        <deviceModel model="deviceModel_514" class="1" name="VR2W" description="VR2W Acoustic receiver" />
    </deviceModelList>

    <recorderList>
        <recorder id="recorder_1" model="deviceModel_514" serial="12345" />
    </recorderList>

    <transmitterList>
        <transmitter id="transmitter_1" tagId="123" codeSpace="1303" frequency="69000"/>
        <transmitter id="transmitter_2" tagId="456" codeSpace="1303" frequency="69000"/>
        <transmitter id="transmitter_3" tagId="789" codeSpace="1303" frequency="69000"/>
    </transmitterList>

    <recordingContextList>
        <recordingContext id="recordingContext_1" rxr="recorder_1" rxrFwVer="123"/>
    </recordingContextList>

    <offloadContextList>
        <offloadContext id="offloadContext_1" dateTime="2012-06-12T12:34:56.000" uuid="550e8400e29b41d4a716446655440000" 
                        offloaderAppName="VUE" offloaderAppVersion="123" offloaderModuleName="VR2W" offloaderModuleVersion="456" />
    </offloadContextList>

    <eventContextList>
        <eventContext id="eventContext_1" recordingContext="recordingContext_1" offloadContext="offloadContext_1"/>
    </eventContextList>

    <detectionList>

'''

def xmlFooter = '''    </detectionList>

</vueOffload>
'''

xmlWriter << xmlHeader

def startTime = System.currentTimeMillis()
def xmlDateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S")
def sqlDateFormatter = new SimpleDateFormat("yyyyMMddHHmmssS")

numDets.times {
	
	i ->
	
	if (i % 100000 == 0)
	{
		println "writing record " + i
	}
	def dateInMillis = startTime + (i * 1000)
	def date = new java.util.Date(dateInMillis)
	
	xmlWriter << '        <detection dateTime="' << xmlDateFormatter.format(date) << '" txr="transmitter_1" rxr="recorder_1" context="eventContext_1" />\n'
	sqlWriter << 'INSERT INTO VS_detection VALUES (' + sqlDateFormatter.format(date) + ',1,1,1);'
}

xmlWriter << xmlFooter

xmlWriter.close()
sqlWriter.close()

