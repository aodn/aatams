import geb.Configuration;
import geb.ConfigurationLoader
import geb.junit4.GebReportingTest


class BaseTests extends GebReportingTest 
{
	Configuration createConf()
	{
		ConfigurationLoader loader = new ConfigurationLoader(gebConfEnv)
		Configuration config = loader.getConf(gebConfScript)
		
		if (!config.reportsDir)
		{
			config = loader.getConf(new File('./test/functional/GebConfig.groovy').toURL(), new GroovyClassLoader())
		}
		
		if (!config.reportsDir)
		{
			config.reportsDir = new File("./target/test-reports/geb")
		}
		
		if (!config.baseUrl)
		{
			config.baseUrl = "http://localhost:8090/aatams/"
		}
		
		return config
	}
}
