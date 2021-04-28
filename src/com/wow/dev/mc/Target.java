package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Target extends Transformation{

	private String transformationName;
	
	public Target(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails) {
		super(map, transformationType, jsonDetails);
	}
	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"TGT");
	}
	
	public String validateTargetType() {
		return (String) ((Map) map.get("transformations.dataAdapter")).get("dataAdapter.typeSystem");
	}
	
	public String validateTargetObjectType() {
		return (String) ((Map) map.get("transformations.dataAdapter")).get("dataAdapter.objectType");
	}
	
	public boolean validatePreSQLQuery(Map<Object, Object> validationList, int i) {
		String preSQL=(String)getAdvanceProperty("Pre SQL");
		if(!(preSQL==null || preSQL.isEmpty())) {
			validationList.put(i+"TARGET.PRE_SQL","Target[" + transformationName + "]. Pre SQL Query is detected in the target.");
			return false;
		}else {
			return true;
		}
	}
	
	public boolean validatePostSQLQuery(Map<Object, Object> validationList, int i) {
		String postSQL=(String)getAdvanceProperty("Post SQL");
		if(!(postSQL==null || postSQL.isEmpty())) {
			validationList.put(i+"TARGET.POST_SQL","Target[" + transformationName + "]. Post SQL Query is detected in the target.");
			return false;
		}else {
			return true;
		}
	}
	
	public boolean validateUpdateOverrideQuery(Map<Object, Object> validationList, int i) {
		String updateOverride=(String)getAdvanceProperty("Update Override");
		if(!(updateOverride==null || updateOverride.isEmpty())) {
			validationList.put(i+"TARGET.UPDATE_OVERRIDE","Target[" + transformationName + "]. update Override Query is detected in the target.");
			return false;
		}else {
			return true;
		}
	}
	
	public boolean validateIsDynamicTargetObject() {
		return Boolean.parseBoolean((String)map.get("transformations.createTarget"));
	}
	
	public String validateFieldMappingMode() {
		return(String)map.get("transformations.fieldMappingMode");
	}
	
	public String validateInputSorted() {
		return(String)map.get("transformations.inputSorted");
	}
	
	public boolean validateTargetCustomQuery(Map<Object, Object> validationList, int i) {
		String customQuery=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.object")).get("object.customQuery"));
		 if(!(customQuery==null || customQuery.isEmpty())) {
			 validationList.put(i+"TARGET.CUSTOM_SQL_QUERY","Target[" + transformationName + "]. Target Custom Query is detected in the Target.");
			 return false;
		 }
		return true;
	}

	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("TARGET_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");
		super.trace(transformationName);
		transformationValidationResults.put("TARGET_TYPE_VALIDATION", validateTargetType());
		transformationValidationResults.put("TARGET_OBJ_TYPE_VALIDATION", validateTargetObjectType());
		transformationValidationResults.put("TARGET_PRE_SQL_VALIDATION", validatePreSQLQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("TARGET_POST_SQL_VALIDATION", validatePostSQLQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("TARGET_OVERRIDE_QUERY_VALIDATION", validateUpdateOverrideQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("TARGET_IS_DYMAIC_TARGET_VALIDATION", validateIsDynamicTargetObject()?"YES":"NO");
		transformationValidationResults.put("TARGET_FIELD_MODE_VALIDATION", validateFieldMappingMode());
		transformationValidationResults.put("TARGET_IS_SORTED_INPUT_VALIDATION", validateInputSorted());
		transformationValidationResults.put("TARGET_CUSTOM_QUERY_VALIDATION", validateTargetCustomQuery(validationList,i)?"PASS":"WARNING");
	}

	public String getTransformationName() {
		return transformationName;
	}

}
