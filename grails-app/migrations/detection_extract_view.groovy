databaseChangeLog = 
{
	changeSet(author: "jburgess", id: "1327300306000-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1332381648000-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				sql.execute ('DROP VIEW IF EXISTS ' + viewName)
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1340674784000-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}
	
	changeSet(author: "jburgess", id: "1353377490000-1")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
				sql.execute ('create or replace view ' + viewName + ' as ' + viewSelect)
			}
		}
	}

	changeSet(author: "jburgess", id: "1353377490000-2")
	{
		grailsChange
		{
			change
			{
				def viewName = application.config.rawDetection.extract.view.name
				def viewSelect = application.config.rawDetection.extract.view.select
                
                sql.execute('create or replace view ' + viewName + ' as ' + viewSelect)
				sql.execute("select drop_matview('detection_extract_view_mv');")
				sql.execute("select create_matview('detection_extract_view_mv', 'detection_extract_view');")
			}
		}
	}
}

