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
}

