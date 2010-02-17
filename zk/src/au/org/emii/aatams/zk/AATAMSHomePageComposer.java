package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tab;

/**
 * This composer will automatically autowire and autoforward your composers.
 * 
 * As it extends Window, you can set both the apply and use window attributes to
 * point to a subclass without having to re-implement the afterCompose method
 * each time.
 * 
 * Also includes a handy logger instances
 * 
 * @author geoff
 * 
 */
public class AATAMSHomePageComposer extends Window implements AfterCompose, Composer {
	public Include centerInclude;
	private Tab home;
	private Tab data_entry;
	private Tab data_search;
	private Tab tools;
	private Tab links;
	private Tab map;
	private Tab help;
	private Tab forms;
	private String WFS_URI = null;

	private static final long serialVersionUID = 1L;

	/**
	 * Log4j instance
	 */
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * This does nothing but is required to keep zk runtime happy - if you
	 * remove it you will get classcast errors when the page loads because we
	 * are required to implement the do Composer interface even though we
	 * already implement AfterCompose
	 */
	public void doAfterCompose(Component arg0) throws Exception {
	}

	/**
	 * Perform the autowiring - courtesy of the zk mvc 3 tutorial
	 */
	public void afterCompose() {
		// wire variables
		Components.wireVariables(this, this);

		// NO need to register onXxx event listeners

		// auto forward
		Components.addForwards(this, this);

		// set an opening page
		String tab = Executions.getCurrent().getParameter("tab");
		if (tab == null) {
			onClick$home(null);
		} else if (tab.matches("home")) {
			onClick$help(null);
		} else if (tab.matches("data_entry")) {
			onClick$data_entry(null);
		} else if (tab.matches("data_search")) {
			onClick$data_search(null);
		} else if (tab.matches("tools")) {
			onClick$tools(null);
		} else if (tab.matches("map")) {
			onClick$map(null);
		} else if (tab.matches("links")) {
			onClick$links(null);
		} else if (tab.matches("forms")) {
			centerInclude.setSrc("index.xml");
		}
	}

	public void onClick$home(Event event) {
		centerInclude.setSrc("home.zul");
	}

	public void onClick$data_entry(Event event) {
		centerInclude.setSrc("data_entry.zul");
	}

	public void onClick$data_search(Event event) {
		centerInclude.setSrc("data_search.zul");
	}

	public void onClick$tools(Event event) {
		centerInclude.setSrc("tools.zul");
	}

	public void onClick$map(Event event) {
		centerInclude.setSrc("wms.zul");
	}

	public void onClick$links(Event event) {
		centerInclude.setSrc("links.zul");
	}

	public void onClick$help(Event event) {
		centerInclude.setSrc("help.html");
	}

	public void onClick$AddTagDevice(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_transmitter_device.xml");
	}

	public void onClick$AddReceiverDevice(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_receiver_device.xml");
	}

	public void onClick$AddDeviceModel(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_device_model.xml");
	}

	public void onClick$AddDeployment(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_receiver_deployment.xml");
	}

	public void onClick$AddRecovery(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_deployment_recovery.xml");
	}

	public void onClick$AddDownload(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_deployment_download_1.xml");
	}

	public void onClick$AddTagRelease(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_tag_release.xml");
	}

	public void onClick$AddSurgery(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_surgery.xml");
	}

	// Data Entry > Other
	public void onClick$AddInstallation(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_installation.xml");
	}

	public void onClick$ModifyInstallation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		// centerInclude.setSrc("xform.zul?form=forms/update_installation.xml");
	}

	public void onClick$AddStation(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_installation_station.xml");
	}

	public void onClick$ModifyStation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		// centerInclude.setSrc("xform.zul?form=forms/update_installation_station.xml");
	}

	public void onClick$AddOrganisation(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_organisation.xml");
	}

	public void onClick$ModifyOrganisation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		// centerInclude.setSrc("xform.zul?form=forms/update_organisation.xml");
	}

	public void onClick$AddPerson(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_person.xml");
	}

	public void onClick$ModifyPerson(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		// centerInclude.setSrc("xform.zul?form=forms/update_person.xml");
	}

	public void onClick$AddProject(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_project.xml");
	}

	public void onClick$ModifyProject(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		// centerInclude.setSrc("xform.zul?form=forms/update_project.xml");
	}

	public void onClick$AddProjectPerson(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_project_person.xml");
	}

	public void onOpen$timeline_plot(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}

	public void onOpen$timeseries_plot(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}

	public void onClick$ReceiverDeploymentsAdvancedSearch(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=deployments");
	}
	
	public void onClick$TagReleasesAdvancedSearch(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=tag_releases");
	}
	
	public void onClick$ReceiversAdvancedSearch(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=receivers");
	}
	
	public void onClick$TagsAdvancedSearch(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=tags");
	}
	
	public void onClick$DetectionsAdvancedSearch(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=detections");
	}
	
	public void onClick$DetectionsAdvancedSearch1(Event event) {
		centerInclude.setSrc("xform.zul?form=search/search.html?_dataset=detections");
	}
	
	public void onClick$ViewDetectionSummaryByInstallation(Event event) {
		centerInclude.setSrc("installation_station_summary.zul");
	}

	public void onClick$ViewDetectionSummaryByClassification(Event event) {
		centerInclude.setSrc("classification_tag_summary.zul");
	}

	public void onClick$ViewDetectionSummaryBytagDevice(Event event) {
		centerInclude.setSrc("project_tag_summary.zul");
	}

	public void onClick$ViewDetectionSummaryByLocation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeploymentDetectionTreeView(Event event) {
		centerInclude.setSrc("deployment_detections_tree.zul");
	}
	
	public void onClick$TagDetectionTreeView(Event event) {
		centerInclude.setSrc("tag_detections_tree.zul");
	}
	
	public void onClick$ClassificationDetectionTreeView(Event event) {
		centerInclude.setSrc("classification_detections_tree.zul");
	}

	public void onClick$ViewTagDevices(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_tags.html");
		centerInclude.setSrc("view_all_tags.zul");
	}

	public void onClick$ViewReceiverDevices(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_receivers.html");
		centerInclude.setSrc("view_all_receivers.zul");
	}

	public void onClick$ViewDeviceModels(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_device_models.html");
		centerInclude.setSrc("view_all_device_models.zul");
	}

	public void onClick$ViewDeploymentsByProject(Event event) {
		centerInclude.setSrc("view_all_receiver_deployments_by_project.zul");
	}

	public void onClick$ViewDeploymentsByOrganisation(Event event) {
		centerInclude.setSrc("view_all_receiver_deployments_by_organisation.zul");
	}

	public void onClick$ViewDeploymentsbyPerson(Event event) {
		centerInclude.setSrc("view_all_receiver_deployments_by_person.zul");
	}

	public void onClick$ViewDeployments(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_receiver_deployments.html");
		centerInclude.setSrc("view_all_receiver_deployments.zul");
	}

	public void onClick$ViewDownloadsByDeployment(Event event) {
		centerInclude.setSrc("view_all_downloads_by_deployment.zul");
	}

	public void onClick$ViewDownloadsByReceiver(Event event) {
		centerInclude.setSrc("view_all_downloads_by_receiver.zul");
	}

	public void onClick$ViewDownloadsByLocation(Event event) {
		centerInclude.setSrc("view_all_downloads_by_location.zul");
	}

	public void onClick$ViewRetrievalsDownloads(Event event) {
		centerInclude.setSrc("view_all_retrievals_and_downloads.zul");
	}

	public void onClick$TagReleasesBySpecies(Event event) {
		centerInclude.setSrc("view_all_tag_releases_by_classification.zul");
	}

	public void onClick$TagReleasesByLocation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}

	public void onClick$ViewInstallations(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_installations.html");
		centerInclude.setSrc("view_all_installations.zul");
	}

	public void onClick$ViewOrganisations(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_organisations.html");
		centerInclude.setSrc("view_all_organisations.zul");
	}

	public void onClick$ViewPeople(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_people.html");
		centerInclude.setSrc("view_all_people.zul");
	}

	public void onClick$ViewProjects(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_projects.html");
		centerInclude.setSrc("view_all_projects.zul");
	}
	
	//WORKSHEETS
	public void onClick$DeploymentRetrievalWorksheets(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_people.html");
		centerInclude.setSrc("xform.zul?form=forms/recovery_worksheets.xml");
	}

	public void onClick$DeploymentDataDownloadWorksheets(Event event) {
		//centerInclude.setSrc("xform.zul?form=forms/view_all_projects.html");
		centerInclude.setSrc("xform.zul?form=forms/download_worksheets.xml");
	}
	
	public void onClick$ModifyTagDevice(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeleteTagDevice(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ModifyTagRelease(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeleteTagRelease(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ModifyReceiverDevice(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeleteReceiverDevice(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ModifyDeployment(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeleteDeployment(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ModifyRecovery(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$DeleteRecovery(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}

	public void onClick$ModifyPeople(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ViewDeploymentsByLocation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
	}
	
	public void onClick$ViewProjectRolePerson(Event event) {
		centerInclude.setSrc("view_all_project_persons.zul");
	}
	
	public void onClick$ViewClassifications(Event event) {
		centerInclude.setSrc("view_all_classifications.zul");
	}
}