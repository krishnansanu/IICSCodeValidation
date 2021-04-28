package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Joiner extends Transformation{
	
	private String transformationName;
	private String folderName;

	public Joiner(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails,String folderName) {
		super(map, transformationType, jsonDetails);
		this.folderName=folderName;
		// TODO Auto-generated constructor stub
	}
	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"JNR");
	}
	
	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}
	
	public boolean validateCacheDirectory(Map<Object, Object> validationList, int i) {
		String cacheDir=(String)getAdvanceProperty("Cache Directory");
		if(cacheDir==null || !cacheDir.contains(folderName)) {
			validationList.put(i+"JOINER.CACHE_DIR","Joiner[" + transformationName + "]. Cache Directory should be pointing to - "+ folderName);
			return false;
		}
		return true;
	}
	
	public String validateJoinerDataCacheSize() {
		return (String)getAdvanceProperty("Joiner Data Cache Size");
		
	}
	
	public String validateJoinerIndexCacheSize() {
		return (String)getAdvanceProperty("Joiner Index Cache Size");
		
	}
	
	public boolean validateSortedInputs() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Sorted Input"));
	}
	
	public String validateJoinType() {
		return (String)map.get("transformations.joinType");
	}
	
	public boolean validateJoinCondition(Map<Object, Object> validationList, int i) {
		String advancedJoinCondition=(String)map.get("transformations.advancedJoinCondition");
		int joinConditionsCount=jsonDetails.getParentCount(map, "transformations.joinConditions");
		
		if(!(advancedJoinCondition==null || advancedJoinCondition.isEmpty()) || joinConditionsCount>0) {
			return true;
		} 
		else {
			validationList.put(i+"JOINER.JOIN_CONDITION","Joiner[" + transformationName + "]. Join condition should not be empty in joiner transformation");
			return false;
		}
	}
	

	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("JOINER_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");
		super.trace(transformationName);
		transformationValidationResults.put("JOINER_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("JOINER_CACHE_DIR_VALIDATION", validateCacheDirectory(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("JOINER_DATA_CACHE_SIZE_VALIDATION", validateJoinerDataCacheSize());
		transformationValidationResults.put("JOINER_INDEX_CACHE_SIZE_VALIDATION", validateJoinerIndexCacheSize());
		transformationValidationResults.put("JOINER_IS_SORTED_INPUTS_VALIDATION", validateSortedInputs()?"YES":"NO");
		transformationValidationResults.put("JOINER_TYPE_VALIDATION", validateJoinType());
		transformationValidationResults.put("JOINER_CONDITION_VALIDATION", validateJoinCondition(validationList,i)?"PASS":"FAIL");
	}

	public String getTransformationName() {
		return transformationName;
	}

}
