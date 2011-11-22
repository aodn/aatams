package pages

import module.AddPersonToProjectDialog

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
		
		addPersonLink { $("a", id:"add_person_to_project") }
		
//		addPersonDialog { module AddPersonToProjectDialog, $("div", id:"dialog-form-add-person") }
		addPersonDialog { module AddPersonToProjectDialog, $("div", id:"dialog-form-add-person").parent() }
//		addPersonDialog { module AddPersonToProjectDialog, $("div", "aria-labelledby":"ui-dialog-title-dialog-form-add-person") }
	}
}
