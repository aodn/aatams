package au.org.emii.aatams.report

import org.codehaus.groovy.grails.scaffolding.GrailsTemplateGenerator;
import org.joda.time.DateTime;

import au.org.emii.aatams.*
import au.org.emii.aatams.detection.ValidDetection;
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import de.micromata.opengis.kml.v_2_2_0.Kml
import grails.test.*
import java.util.zip.ZipFile

class KmlServiceTests extends GrailsUnitTestCase
{
	def service
	def slurper = new XmlSlurper()
	
	Project sealProject, whaleProject
	Installation tasmanIsland, ningaloo
	InstallationStation tasmanSW1, tasmanSW2, ningalooW, ningalooE
	Receiver rx1, rx2, rx3, rx4
	ReceiverDeployment deploymentTasmanSW1, deploymentTasmanSW2, deploymentNingalooW, deploymentNingalooE
	 
    protected void setUp() 
	{
        super.setUp()
		
		mockLogging(KmlService, true)
		service = new KmlService()
		
		sealProject = new Project(name:"Seals")
		whaleProject = new Project(name:"Whales")
		mockDomain(Project, [sealProject, whaleProject])
		
		tasmanIsland = new Installation(name: "Tasman Island", project:sealProject)
		ningaloo = new Installation(name: "Ningaloo", project:whaleProject)
		mockDomain(Installation, [tasmanIsland, ningaloo])
		sealProject.addToInstallations(tasmanIsland)
		whaleProject.addToInstallations(ningaloo)
		
		tasmanSW1 = 
			new InstallationStation(name: "SW1", 
									installation:tasmanIsland, 
									location: new GeometryFactory().createPoint(new Coordinate(34f, 34f)))
		tasmanSW2 = 
			new InstallationStation(name: "SW2", 
									installation:tasmanIsland, 
									location: new GeometryFactory().createPoint(new Coordinate(12f, 12f)))
			
		ningalooW =
			new InstallationStation(name: "W",
									installation: ningaloo,
									location: new GeometryFactory().createPoint(new Coordinate(56f, 56f)))
		ningalooE =
			new InstallationStation(name: "E",
									installation: ningaloo,
									location: new GeometryFactory().createPoint(new Coordinate(67f, 67f)))

		def stationList = [tasmanSW1, tasmanSW2, ningalooW, ningalooE]

		mockDomain(InstallationStation, stationList)
		InstallationStation.metaClass.toKmlDescription = { "some description" }
		[tasmanSW1, tasmanSW2].each { tasmanIsland.addToStations(it) }
		[ningalooW, ningalooE].each { ningaloo.addToStations(it) }
			
		rx1 = new Receiver(serialNumber: "VR2W-111")
		rx2 = new Receiver(serialNumber: "VR2W-222")
		rx3 = new Receiver(serialNumber: "VR2W-333")
		rx4 = new Receiver(serialNumber: "VR2W-444")
		def receiverList = [rx1, rx2, rx3, rx4]

		mockDomain(Receiver, receiverList)
		receiverList.each { it.save() }
		
		deploymentTasmanSW1 = 
			new ReceiverDeployment(receiver:rx1,
								   station:tasmanSW1,
								   deploymentDateTime:new DateTime("2011-11-12T12:34:12"))
		deploymentTasmanSW2 = 
			new ReceiverDeployment(receiver:rx2,
								   station:tasmanSW2,
								   deploymentDateTime:new DateTime("2011-11-12T13:34:12"))
		deploymentNingalooW = 
			new ReceiverDeployment(receiver:rx3,
								   station:ningalooW,
								   deploymentDateTime:new DateTime("2011-11-12T12:34:12"))
		deploymentNingalooE = 
			new ReceiverDeployment(receiver:rx4,
								   station:ningalooW,
								   deploymentDateTime:new DateTime("2011-11-12T13:34:12"))
		mockDomain(ReceiverDeployment, [deploymentTasmanSW1, deploymentTasmanSW2, deploymentNingalooW, deploymentNingalooE])
		
		[sealProject, whaleProject].each { it.save() }
		
		service.metaClass.getMainCssStream =
		{
			return new FileInputStream(new File("web-app/css/main.css"))
		}
		
		service.metaClass.getImosLogoStream =
		{
			return new FileInputStream(new File("web-app/images/IMOS-logo.png"))
		}
		
		service.metaClass.getFishIconStream =
		{
			return new FileInputStream(new File("web-app/images/fish.png"))
		}

		service.metaClass.getStationIconStream =
		{
			return new FileInputStream(new File("web-app/images/station.png"))
		}

		service.metaClass.getCircleIconStream =
		{
			return new FileInputStream(new File("web-app/images/circle.png"))
		}
    }

    protected void tearDown() 
	{
        super.tearDown()
    }

    void testEmptyList() 
	{
		List<ValidDetection> detections = Collections.EMPTY_LIST
		
		def parsedKml = convertToParsedKml(detections)
		assertNotNull(parsedKml)
		
		def allNodes = parsedKml.depthFirst().collect{ it }
		def kmlNode = allNodes.find { it.name() == "kml" }
		
		assertNotNull(kmlNode)
    }
	
	void testOneValidDetection()
	{
		def parsedKml = convertToParsedKml(InstallationStation.list())
		assertNotNull(parsedKml)
		
		def allNodes = parsedKml.depthFirst().collect{ it }
		def folderNodes = allNodes.findAll { it.name() == "Folder" }
		
		[sealProject.name, tasmanIsland.name, whaleProject.name, ningaloo.name].eachWithIndex
		{
			name, i ->
			
			assertEquals(name, folderNodes[i].name.text())
		}
		
		def placemarkNodes = allNodes.findAll { it.name() == "Placemark" }
		assertEquals(4, placemarkNodes.size())
		
		(tasmanIsland.stations + ningaloo.stations).each
		{
			station ->
			
			assertTrue(placemarkNodes*.name*.text().contains(station.name))
		}
	}

	void testTwoDetectionsDifferentStations()
	{
		def parsedKml = convertToParsedKml(InstallationStation.list())
		assertNotNull(parsedKml)
		
		def allNodes = parsedKml.depthFirst().collect{ it }
		
		def placemarkNodes = allNodes.findAll { it.name() == "Placemark" }
		
		assertEquals(4, placemarkNodes.size())
		
		def tasmanSW1Node = placemarkNodes.find { it.name == tasmanSW1.name }
		assertNotNull(tasmanSW1Node)
		assertEquals("1", tasmanSW1Node.open.text())
		assertEquals("34.0,34.0", tasmanSW1Node.Point.coordinates.text())

		def tasmanSW2Node = placemarkNodes.find { it.name == tasmanSW2.name }
		assertNotNull(tasmanSW2Node)
		assertEquals("1", tasmanSW2Node.open.text())
		assertEquals("12.0,12.0", tasmanSW2Node.Point.coordinates.text())
	}

	void testGenerateKmz()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream()
		def kml = new Kml()
		kml.createAndSetDocument()
		
		service.generateKmz(kml, out)
		
		assertFalse(out.size() == 0)
		
		File tmpKmz = File.createTempFile("kmz", ".zip")
		FileOutputStream kmzOut = new FileOutputStream(tmpKmz)
		kmzOut.write(out.toByteArray())
		kmzOut.close()
		
		ZipFile kmzFile = new ZipFile(tmpKmz)
		
		assertEquals(["doc.kml", "files/", "files/main.css", "files/IMOS-logo.png", "files/fish.png", "files/station.png", "files/circle.png"], kmzFile.entries().toList()*.name)
	}
	
	void testIsSupportedFormat()
	{
		assertFalse(service.isSupportedFormat())	
		assertFalse(service.isSupportedFormat("PDF"))
		assertFalse(service.isSupportedFormat("CSV"))
		assertTrue(service.isSupportedFormat("KML"))
		assertTrue(service.isSupportedFormat("KMZ"))
		assertTrue(service.isSupportedFormat("KMZ (tag tracks)"))
		assertTrue(service.isSupportedFormat("KMZ (bubble plot)"))
	}
	
	private def convertToParsedKml(List stations) 
	{
		def kml = service.toKml(stations)

		OutputStream kmlStream = new ByteArrayOutputStream()
		kml.marshal(kmlStream)

		def parsedKml = slurper.parseText(kmlStream.toString("UTF-8"))

		return parsedKml
	}
}
