package com.wow.dev.mc;

import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class Source extends Transformation{
	
	private String transformationName;
	
	public Source(Map<Object,Object> map, String transformationType,ExtractJSONDetails jsonDetails) {
		super(map, transformationType,jsonDetails);
	}
	
	public boolean validateTransforamtionName(Map<Object,Object> validationList, int i) {
		this.transformationName=(String)map.get("transformations.name");
		return super.validateTransforamtionName(transformationName, validationList, i,3,"SRC");
	}

	public boolean validatetracingLevel(Map<Object, Object> validationList, int i) {
		String tracingLevel=(String)getAdvanceProperty("Tracing Level");
		if(tracingLevel==null) return true;
		else return super.validatetracingLevel(tracingLevel, transformationName, validationList, i);
	}
	
	public boolean validatePreSQLQuery(Map<Object, Object> validationList, int i) {
		String preSQLQuery=(String)getAdvanceProperty("Pre SQL");
		if(!(preSQLQuery==null || preSQLQuery.isEmpty())) {
			validationList.put(i+"SOURCE.PRE_SQL_QUERY","Source[" + transformationName + "]. Pre-SQL Query is detected in the Source.");
			return false;
		}
		return true;
	}
	
	public boolean validatePostSQLQuery(Map<Object, Object> validationList, int i) {
		String postSQLQuery=(String)getAdvanceProperty("Post SQL");
		if(!(postSQLQuery==null || postSQLQuery.isEmpty())) {
			validationList.put(i+"SOURCE.POST_SQL_QUERY","Source[" + transformationName + "]. Post-SQL Query is detected in the Source.");
			return false;
		}
		return true;
	}
	
	public boolean validateOverrideSQLQuery(Map<Object, Object> validationList, int i) {
		String SQLQuery=((String)((Map)((Map)map.get("transformations.dataAdapter")).get("dataAdapter.object")).get("object.customQuery"));
		 if(!(SQLQuery==null || SQLQuery.isEmpty())) {
			 validationList.put(i+"SOURCE.OVERRIDE_SQL_QUERY","Source[" + transformationName + "]. SQL Override Query is detected in the Source.");
			 return false;
		 }
		return true;
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
	public void validate(Map<Object,Object> validationList, int i) {
		transformationValidationResults.put("SOURCE_NAME_VALIDATION", validateTransforamtionName(validationList,i)?"PASS":"FAIL");
		super.trace(transformationName);
		transformationValidationResults.put("SOURCE_TRACING_LEVEL_VALIDATION", validatetracingLevel(validationList,i)?"PASS":"FAIL");
		transformationValidationResults.put("SOURCE_PRE_SQL_VALIDATION", validatePreSQLQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("SOURCE_POST_SQL_VALIDATION", validatePostSQLQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("SOURCE_OVERRIDE_SQL_VALIDATION", validateOverrideSQLQuery(validationList,i)?"PASS":"WARNING");
		transformationValidationResults.put("SOURCE_FILTER_CONDITION_VALIDATION", validateSourceFilterCondition()?"YES":"NO");
		transformationValidationResults.put("SOURCE_SORT_CONDITION_VALIDATION", validateSourceSortCondition()?"YES":"NO");
		transformationValidationResults.put("SOURCE_IS_DISTINCT_VALIDATION", validateSourceDistinctRecord()?"YES":"NO");
	}

	public String getTransformationName() {
		return transformationName;
	}
	
}

