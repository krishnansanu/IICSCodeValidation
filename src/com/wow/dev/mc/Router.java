package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Router extends Transformation{
	
	private String transformationName;
	
	public Router(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails) {
		super(map, transformationType, jsonDetails);
		// TODO Auto-generated constructor stub
	}

	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"RTR");
	}
	
	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		if(tracingLevel==null)return true;
		else return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}
	
	public int validateRouterCategories() {
		return jsonDetails.getParentCount(map, "transformations.groupFilterConditions");
	}
	
	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("ROUTER_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"WARNING");
		super.trace(transformationName);
		transformationValidationResults.put("ROUTER_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("ROUTER_CATEGORY_VALIDATION", Integer.toString(validateRouterCategories()));
	}

	public String getTransformationName() {
		return transformationName;
	}

}
