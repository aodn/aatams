package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
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
	private Tab forms;

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
	
	public void onClick$links(Event event) {
		centerInclude.setSrc("links.zul");
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

	public void onClick$ModifyDeployment(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		//centerInclude.setSrc("xform.zul?form=forms/modify_receiver_deployment.xml");
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
		//centerInclude.setSrc("xform.zul?form=forms/update_installation.xml");
	}
	
	public void onClick$AddStation(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_installation_station.xml");
	}

	public void onClick$ModifyStation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		//centerInclude.setSrc("xform.zul?form=forms/update_installation_station.xml");
	}

	public void onClick$AddOrganisation(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_organisation.xml");
	}

	public void onClick$ModifyOrganisation(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		//centerInclude.setSrc("xform.zul?form=forms/update_organisation.xml");
	}

	public void onClick$AddPerson(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_person.xml");
	}

	public void onClick$ModifyPerson(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		//centerInclude.setSrc("xform.zul?form=forms/update_person.xml");
	}

	public void onClick$AddProject(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_project.xml");
	}

	public void onClick$ModifyProject(Event event) {
		centerInclude.setSrc("not_implemented.zul");
		//centerInclude.setSrc("xform.zul?form=forms/update_project.xml");
	}

	public void onClick$AddProjectPerson(Event event) {
		centerInclude.setSrc("xform.zul?form=forms/create_project_person.xml");
	}

	public void onOpen$timeline_plot(Event event) {
		centerInclude.setSrc("timeline.zul");
	}

	public void onOpen$timeseries_plot(Event event) {
		centerInclude.setSrc("timeseries.zul");
	}
}