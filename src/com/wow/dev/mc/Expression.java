package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;


public class Expression extends Transformation{
	
	private String transformationName;
	
	public Expression(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails) {
		super(map, transformationType, jsonDetails);

	}

	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"EXP");
	}

	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("EXPRESSION_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"WARNING");
		super.trace(transformationName);
	}

	public String getTransformationName() {
		return transformationName;
	}
	
}
