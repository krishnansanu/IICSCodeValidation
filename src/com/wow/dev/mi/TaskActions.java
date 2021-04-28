package com.wow.dev.mi;

import java.util.Map;

public class TaskActions {
	
	private String taskActions;
	
	public TaskActions(Map<Object,Object> taskActionDetails) {
//		taskActions=(String) taskActionDetails.get("taskActions.actionType");
		taskActions=(String) taskActionDetails.get("taskActions.type");
	}
	
	public String getTaskActions() {
		return taskActions;
	}

}
