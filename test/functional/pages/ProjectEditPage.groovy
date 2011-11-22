package pages

class ProjectEditPage extends ProjectCreateEditPage 
{
	static url = "project/edit"
	
	static at =
	{
		title == "Edit Project"
	}
	
	static content =
	{
		updateButton (to: [ProjectShowPage]) { $("input", value:"Update") }
		deleteButton (to: [ProjectListPage]) { $("input", value:"Delete") }
	}
}
