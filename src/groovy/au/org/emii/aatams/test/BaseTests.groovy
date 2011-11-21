package au.org.emii.aatams.test

import geb.Configuration;
import geb.ConfigurationLoader
import geb.junit4.GebReportingTest

import pages.*

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

	protected void login(username, password) 
	{
		to HomePage
		
		assert heading.text() == "Australian Animal Tagging and Monitoring System (AATAMS)"
		loginLink.click()
		
		assert at(LoginPage)
		usernameTextField << username
		passwordTextField << password
	
		signInButton.click()
	}

	protected void loginAsSysAdmin() 
	{
		login("jkburges", "password")
	}
}
