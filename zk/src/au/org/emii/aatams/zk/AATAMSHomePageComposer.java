package au.org.emii.aatams.zk;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Iframe;

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
	public Iframe iframe;
	private String xformsPath = "http://test.emii.org.au/aatams/forms";

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

	public void setIFrameSrc(String source) {
		iframe.setSrc(source);
	}

	public void onClick$AddTagDevice(Event event) {
		iframe.setSrc(this.xformsPath + "create_transmitter_device.xml");
	}

	public void onClick$AddReceiverDevice(Event event) {
		iframe.setSrc(this.xformsPath + "create_receiver_device.xml");
	}

	public void onClick$AddDeviceModel(Event event) {
		iframe.setSrc(this.xformsPath + "create_device_model.xml");
	}

	public void onClick$AddDeployment(Event event) {
		iframe.setSrc(this.xformsPath + "create_receiver_deployment.xml");
	}

	public void onClick$ModifyDeployment(Event event) {
		iframe.setSrc(this.xformsPath + "modify_receiver_deployment.xml");
	}
	
	public void onClick$AddRecovery(Event event) {
		iframe.setSrc(this.xformsPath + "create_deployment_recovery.xml");
	}

	public void onClick$AddDownload(Event event) {
		iframe.setSrc(this.xformsPath + "create_deployment_download.xml");
	}
	
	public void onClick$AddTagRelease(Event event) {
		iframe.setSrc(this.xformsPath + "create_tag_release.xml");
	}

	public void onClick$AddSurgery(Event event) {
		iframe.setSrc(this.xformsPath + "create_surgery.xml");
	}

	// Data Entry > Other
	public void onClick$AddInstallation(Event event) {
		iframe.setSrc(this.xformsPath + "create_installation.xml");
	}

	public void onClick$ModifyInstallation(Event event) {
		iframe.setSrc(this.xformsPath + "update_installation.xml");
	}
	
	public void onClick$AddStation(Event event) {
		iframe.setSrc(this.xformsPath + "create_installation_station.xml");
	}

	public void onClick$ModifyStation(Event event) {
		iframe.setSrc(this.xformsPath + "update_installation_station.xml");
	}

	public void onClick$AddOrganisation(Event event) {
		iframe.setSrc(this.xformsPath + "create_organisation.xml");
	}

	public void onClick$ModifyOrganisation(Event event) {
		iframe.setSrc(this.xformsPath + "update_organisation.xml");
	}

	public void onClick$AddPerson(Event event) {
		iframe.setSrc(this.xformsPath + "create_person.xml");
	}

	public void onClick$ModifyPerson(Event event) {
		iframe.setSrc(this.xformsPath + "update_person.xml");
	}

	public void onClick$AddProject(Event event) {
		iframe.setSrc(this.xformsPath + "create_project.xml");
	}

	public void onClick$ModifyProject(Event event) {
		iframe.setSrc(this.xformsPath + "update_project.xml");
	}

	public void onClick$AddProjectPerson(Event event) {
		iframe.setSrc(this.xformsPath + "create_project_person.xml");
	}

	public void onOpen$timeline_plot(Event event) {
		iframe.setSrc(this.xformsPath + "timeline.zul");
	}

	public void onOpen$timeseries_plot(Event event) {
		iframe.setSrc(this.xformsPath + "timeseries.zul");
	}

}

