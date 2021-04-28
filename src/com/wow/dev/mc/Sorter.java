package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Sorter extends Transformation{
	
	private String transformationName;
	private String folderName;
	
	public Sorter(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails,String folderName) {
		super(map, transformationType, jsonDetails);
		this.folderName=folderName;
		// TODO Auto-generated constructor stub
	}

	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"SRT");
	}
	
	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}

	public boolean validateCacheDirectory(Map<Object, Object> validationList, int i) {
		String cacheDir=(String)getAdvanceProperty("Work Directory");
		if(cacheDir==null || !cacheDir.contains(folderName)) {
			validationList.put(i+"LOOKUP.CACHE_DIR","Lookup[" + transformationName + "]. Cache Directory should be pointing to - "+ folderName);
			return false;
		}
		return true;
	}
	
	public String CacheSizeValidation() {
		return (String)getAdvanceProperty("Sorter Cache Size");
	}
	
	public boolean isDistinctValidation() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Distinct"));
	}
	
	public String validateSorterMode() {
		return (String)map.get("transformations.sorterMode");
	}
	
	public boolean validateSorterCondition(Map<Object, Object> validationList, int i) {
		String advancedSort=(String)map.get("transformations.advancedSort");
		int sortEntriesCount=jsonDetails.getParentCount(map, "transformations.sortEntries");
		if(!(advancedSort==null || advancedSort.isEmpty()) || sortEntriesCount>0) {
			return true;
		} 
		else {
			validationList.put(i+"SORTER.SORT_CONDITION","Sorter[" + transformationName + "]. condition should not be empty");
			return false;
		}
	}
	
	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("SORTER_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");	
		super.trace(transformationName);
		transformationValidationResults.put("SORTER_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("SORTER_CACHE_DIR_VALIDATION", validateCacheDirectory(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("SORTER_CACHE_SIZE_VALIDATION", CacheSizeValidation());
		transformationValidationResults.put("SORTER_IS_DISTINCT_INPUT_VALIDATION", isDistinctValidation()?"YES":"NO");
		transformationValidationResults.put("SORTER_MODE_VALIDATION", validateSorterMode());
		transformationValidationResults.put("SORTER_CONDITION_VALIDATION", validateSorterCondition(validationList,i)?"PASS":"FAIL");
	}


	public String getTransformationName() {
		return transformationName;
	}

}
