package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Aggregator extends Transformation{

	
	private String transformationName;
	private String folderName;
	
	
	public Aggregator(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails,String folderName) {
		super(map, transformationType, jsonDetails);
		this.folderName=folderName;
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"AGG");
	}
	
	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}

	public boolean validateCacheDirectory(Map<Object, Object> validationList, int i) {
		String cacheDir=(String)getAdvanceProperty("Cache Directory");
		if(cacheDir==null || !cacheDir.contains(folderName)) {
			validationList.put(i+"LOOKUP.CACHE_DIR","Lookup[" + transformationName + "]. Cache Directory should be pointing to - "+ folderName);
			return false;
		}
		return true;
	}
	
	public String dataCacheSizeValidation() {
		return (String)getAdvanceProperty("Aggregator Data Cache Size");
	}
	
	public String indexCacheSizeValidation() {
		return(String)getAdvanceProperty("Aggregator Index Cache Size");
	}
	
	public boolean isSortedInputValidation() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Sorted Input"));
	}

	public boolean groupByFieldsValidation() {
		int groupByFieldsCount=jsonDetails.getParentCount((Map)map.get("transformations.groupByFieldsList"), "groupByFieldsList.fields");
		return groupByFieldsCount>0?true:false;
	}
	
	public boolean aggregationFieldsValidation() {
		int agreegationFieldsCount=jsonDetails.getParentCount(map, "transformations.fields");
		return agreegationFieldsCount>0?true:false;
	}
	
	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("AGGREGATOR_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");
		super.trace(transformationName);
		transformationValidationResults.put("AGGREGATOR_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("AGGREGATOR_CACHE_DIR_VALIDATION", validateCacheDirectory(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("AGGREGATOR_DATA_CACHE_SIZE_VALIDATION", dataCacheSizeValidation());
		transformationValidationResults.put("AGGREGATOR_INDEX_CACHE_SIZE_VALIDATION", indexCacheSizeValidation());
		transformationValidationResults.put("AGGREGATOR_IS_SORTED_INPUT_VALIDATION", isSortedInputValidation()?"YES":"NO");
		transformationValidationResults.put("AGGREGATOR_GROUP_BY_FIELD_VALIDATION", groupByFieldsValidation()?"DETECTED":"N/A");
		transformationValidationResults.put("AGGREGATOR_FILELD_VALIDATION", aggregationFieldsValidation()?"DETECTED":"N/A");
	}


	public String getTransformationName() {
		return transformationName;
	}


}
