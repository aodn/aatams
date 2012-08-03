#!/usr/bin/env groovy

import java.text.SimpleDateFormat

assert (this.args.size() == 2): "Run with <numdetections> <outFilePrefix>"

def numDets = Integer.valueOf(this.args[0])

def xmlFile = new File(this.args[1] + ".xml")
def xmlWriter = xmlFile.newWriter()

def sqlFile = new File(this.args[1] + ".sql")
def sqlWriter = sqlFile.newWriter()

def xmlHeader = '''<?xml version="1.0" encoding="UTF-8"?>

<vueOffload xmlns="http://vemco.com/vue"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://vemco.com/vue vue.xsd">

    <deviceModelList>
        <deviceModel model="514" class="1" name="VR2W" description="VR2W Acoustic receiver" />
    </deviceModelList>

    <recorderList>
        <recorder id="1" model="514" serial="12345" />
    </recorderList>

    <transmitterList>
        <transmitter id="1" tagId="123" codeSpace="1303" frequency="69000"/>
        <transmitter id="2" tagId="456" codeSpace="1303" frequency="69000"/>
        <transmitter id="3" tagId="789" codeSpace="1303" frequency="69000"/>
    </transmitterList>

    <recordingContextList>
        <recordingContext id="1" rxr="1" rxrFwVer="123"/>
    </recordingContextList>

    <offloadContextList>
        <offloadContext id="1" dateTime="2012-06-12T12:34:56.000" uuid="550e8400e29b41d4a716446655440000" 
                        offloaderAppName="VUE" offloaderAppVersion="123" offloaderModuleName="VR2W" offloaderModuleVersion="456" />
    </offloadContextList>

    <eventContextList>
        <eventContext id="1" recordingContext="1" offloadContext="1"/>
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
	
	xmlWriter << '        <detection dateTime="' << xmlDateFormatter.format(date) << '" txr="' + (i % 100) + '" rxr="" txr="' + (i % 100) + '" context="1" />\n'
	sqlWriter << 'INSERT INTO VS_detection VALUES (' + sqlDateFormatter.format(date) + ',' + (i % 100) + ',' + (i % 100) + ',1);\n'
}

xmlWriter << xmlFooter

xmlWriter.close()
sqlWriter.close()

