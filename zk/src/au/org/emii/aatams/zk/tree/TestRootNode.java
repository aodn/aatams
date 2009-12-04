package au.org.emii.aatams.zk.tree;

public class TestRootNode extends RootNode {

	public TestRootNode() {
		super(DetectionsTreeMode.DEPLOYMENTS);
		this.init();
	}

	protected void init() {
		InstallationNode inst1 = (InstallationNode)super.addChild(new InstallationNode(this, this.mode, 2, "Installation1"));
		InstallationNode inst2 = (InstallationNode)super.addChild(new InstallationNode(this, this.mode,3, "Installation2"));
		ReceiverDeploymentNode depl1 = (ReceiverDeploymentNode)inst1.addChild(new ReceiverDeploymentNode(inst1, this.mode,4, "Deployment1"));
		TagNode tag1 = (TagNode)depl1.addChild(new TagNode(depl1, this.mode, 5, "Tag1")); 
		TagNode tag2 = (TagNode)depl1.addChild(new TagNode(depl1, this.mode, 6, "Tag2"));
		TagNode tag3 = (TagNode)depl1.addChild(new TagNode(depl1, this.mode, 7, "Tag3"));
		TagNode tag4 = (TagNode)depl1.addChild(new TagNode(depl1, this.mode, 8, "Tag4"));
	}
}