package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Lookup extends Transformation{

	private String transformationName;
	private String folderName;
	
	public Lookup(Map<Object, Object> map, String transformationType, ExtractJSONDetails jsonDetails,String folderName) {
		super(map, transformationType, jsonDetails);
		this.folderName=folderName;
	}

	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"LKP");
	}
	
	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}

	public boolean validateCacheDirectory(Map<Object, Object> validationList, int i) {
		String cacheDir=(String)getAdvanceProperty("Lookup cache directory name");
		if(cacheDir==null || !cacheDir.contains(folderName)) {
			validationList.put(i+"LOOKUP.CACHE_DIR","Lookup[" + transformationName + "]. Cache Directory should be pointing to - "+ folderName);
			return false;
		}
		return true;
	}
	
	
	public boolean isPersistentCacheValidation() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Lookup cache persistent"));
	}
	
	public boolean isDynamicCacheValidation() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Re-cache from lookup source"));
	}
	
	public String dataCacheSizeValidation() {
		return (String)getAdvanceProperty("Lookup Data Cache Size");
	}
	
	public String indexCacheSizeValidation() {
		return(String)getAdvanceProperty("Lookup Index Cache Size");
	}
	
	public boolean isSortedInputValidation() {
		return Boolean.parseBoolean((String)getAdvanceProperty("Sorted Input"));
	}
	
	public String validateLookupPolicyMatch() {
		return (String)map.get("transformations.multipleMatchPolicy");
	}
	
	public boolean validateLookupType() {
		return Boolean.parseBoolean((String)map.get("transformations.unconnected"));
	}
	
	public boolean validateLookupCondition(Map<Object, Object> validationList, int i){
		String lookupCondition=(String)map.get("transformations.lookupCondition");
		int lookupConditionsCount=jsonDetails.getParentCount(map, "transformations.lookupConditions");
		
		if(!(lookupCondition==null || lookupCondition.isEmpty()) || lookupConditionsCount>0) {
			return true;
		} 
		else {
			validationList.put(i+"LOOKUP.CONDITION","Lookup[" + transformationName + "]. condition should not be empty.");
			return false;
		}
		
	}
	
	public boolean validateSourceFilterCondition() {
		String filCondition=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")).get("readOptions.filterCondition"));
		String advFilCondition=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")).get("readOptions.advancedFilterCondition"));
		int filConditionsCount=jsonDetails.getParentCount(((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")), "readOptions.filterConditions");
		 if(!(filCondition==null || filCondition.isEmpty())  || !(advFilCondition==null || advFilCondition.isEmpty()) || filConditionsCount>0) {
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public boolean validateSourceSortCondition() {
		String sortFieldValues=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")).get("readOptions.sortFieldValues"));
		int sortFieldsCount=jsonDetails.getParentCount(((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")), "readOptions.sortFields");
		 if(!(sortFieldValues==null || sortFieldValues.isEmpty()) || sortFieldsCount>0) {
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public boolean validateSourceDistinctRecord() {
		String distinct=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.readOptions")).get("readOptions.selectDistinct"));
		return Boolean.parseBoolean(distinct);
	}
	

	@Override
	public void validate(Map<Object, Object> validationList, int i) {
		transformationValidationResults.put("LOOKUP_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");
		super.trace(transformationName);
		transformationValidationResults.put("LOOKUP_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("LOOKUP_CACHE_DIR_VALIDATION", validateCacheDirectory(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("LOOKUP_IS_PERSISTENT_CAHCE_VALIDATION", isPersistentCacheValidation()?"YES":"NO");
		transformationValidationResults.put("LOOKUP_IS_DYNAMIC_CACHE_VALIDATION", isDynamicCacheValidation()?"YES":"NO");
		transformationValidationResults.put("LOOKUP_DATA_CACHE_SIZE_VALIDATION", dataCacheSizeValidation());
		transformationValidationResults.put("LOOKUP_INDEX_CACHE_SIZE_VALIDATION", indexCacheSizeValidation());
		transformationValidationResults.put("LOOKUP_IS_SORTED_INPUT_VALIDATION", isSortedInputValidation()?"YES":"NO");
		transformationValidationResults.put("LOOKUP_POLICY_ON_MULTIPLE_MATCH_VALIDATION", validateLookupPolicyMatch());
		transformationValidationResults.put("LOOKUP_TYPE_VALIDATION", validateLookupType()?"UnConnected":"Connected");
		transformationValidationResults.put("LOOKUP_CONDITION_VALIDATION", validateLookupCondition(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("LOOKUP_SOURCE_FILTER_CONDITION_VALIDATION", validateSourceFilterCondition()?"YES":"NO");
		transformationValidationResults.put("LOOKUP_SOURCE_SORT_CONDITION_VALIDATION", validateSourceSortCondition()?"YES":"NO");
		transformationValidationResults.put("LOOKUP_SOURCE_DISTINCT_RECORD_VALIDATION", validateSourceDistinctRecord()?"YES":"NO");
	}
	
	public String getTransformationName() {
		return transformationName;
	}

}
