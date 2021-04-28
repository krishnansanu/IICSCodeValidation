package com.wow.dev.mi;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class MassIngestion {
	
	private String massIngestionName;
	private String massIngestionID;
	private String agentGroupID;
	private String massIngestiondescription;
	private String massIngestionlogLevel;
	private String folderName;
	private Source source;
	private Target target;
	private String postTaskActions;
	
	
	public MassIngestion(Map<Object,Object> jsonMapDetails,ExtractJSONDetails jsonDetails,String folderName) {
		
		Map<Object,Object> massIngestionDetails=(Map<Object, Object>) jsonMapDetails.get("root.mi");
		this.massIngestionName=(String) massIngestionDetails.get("mi.name");
//		this.massIngestionID=(String) massIngestionDetails.get("mi.id");
		this.massIngestionID=(String) massIngestionDetails.get("mi.icsGuid");
//		this.agentGroupID=(String) massIngestionDetails.get("mi.agentGroupId");
		this.agentGroupID=(String) massIngestionDetails.get("mi.agentGroup");
		this.massIngestiondescription=(String) massIngestionDetails.get("mi.description");
		this.massIngestionlogLevel=(String) massIngestionDetails.get("mi.logLevel");
//		this.folderName=(new Location((Map) massIngestionDetails.get("mi.location"))).getLocation();
		this.folderName=folderName;
		this.source=new Source((Map) massIngestionDetails.get("mi.sourceConnection"), (Map) massIngestionDetails.get("mi.sourceOptions"));
		this.target=new Target((Map) massIngestionDetails.get("mi.targetConnection"), (Map) massIngestionDetails.get("mi.targetOptions"));
		
		int taskActionCount=jsonDetails.getChildCount(jsonMapDetails, "taskActions");
		TaskActions taskActions[]=taskActions=new TaskActions[taskActionCount];
		for (int i=0;i<taskActionCount;i++) {
			if(massIngestionDetails.get("mi.taskActions("+i+")")!=null) {
				taskActions[i]=new TaskActions((Map) massIngestionDetails.get("mi.taskActions("+i+")"));
				if(postTaskActions==null)postTaskActions=taskActions[i].getTaskActions(); else postTaskActions+=","+taskActions[i].getTaskActions();
			}
		}
		
	}

	public String getMassIngestionName() {
		return massIngestionName;
	}

	public String getMassIngestionID() {
		return massIngestionID;
	}

	public String getAgentGroupID() {
		return agentGroupID;
	}

	public String getMassIngestiondescription() {
		return massIngestiondescription;
	}

	public String getMassIngestionlogLevel() {
		return massIngestionlogLevel;
	}

	public String getFolderName() {
		return folderName;
	}

	public Source getSource() {
		return source;
	}

	public Target getTarget() {
		return target;
	}
	
	
	public String getPostTaskActions() {
		return postTaskActions;
	}
	
	@Override
	public String toString() {
		return "["+massIngestionName+", "+massIngestionID+", "+agentGroupID+", "+massIngestiondescription+", "+massIngestionlogLevel+", "+folderName+", "+source+", "+target+ ", "+ postTaskActions + "]";
	}
	
}
