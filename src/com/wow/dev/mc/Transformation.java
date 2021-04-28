package com.wow.dev.mc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public abstract class Transformation {
	
	protected Map<Object,Object> map;
	protected Map<String, String> transformationValidationResults;
	protected String transformationType;
	protected ExtractJSONDetails jsonDetails;

	
	public Transformation(Map<Object,Object> map,String transformationType,ExtractJSONDetails jsonDetails) {
		this.map=map;
		this.transformationType=transformationType;
		this.jsonDetails=jsonDetails;
		transformationValidationResults=new LinkedHashMap<String, String>();
	}
	
	public Map<Object,Object> getMap() {
		return map;
	}

	public abstract void validate(Map<Object,Object> validationList,int i);
	
	public String  getTransformationType() {
		return transformationType;
	}
	
	
	
	public boolean validateTransforamtionName(String transformationName,Map<Object,Object> validationList, int i,int endIndex, String prefixValue) {
		if(!transformationName.substring(0, endIndex).equalsIgnoreCase(prefixValue)) {
			validationList.put(i+"_TRANSFORMATION.NAME","Invalid Start of "+transformationType+" Name [" + transformationName + "]. "+transformationType+" Name Should Start with '"+prefixValue+"'");
			return false;
		}
		
		return true;
		
	}
	
	public boolean validatetracingLevel(String tracingLevel,String transformationName,Map<Object,Object> validationList, int i) {
		if(!tracingLevel.equalsIgnoreCase("Normal")) {
			validationList.put(i+"_TRANSFORMATION_TABLEATTRIBUTE.Tracing Level","Transofrmation [" + transformationName + "] tracing level should be Normal");
			return false;
		}
		
		return true;
	}
	
	public String getAdvanceProperty(String propertyName) {
		int advanceSessionPropertyCount= jsonDetails.getParentCount(map, "transformations.advancedProperties");
		for(int i=0;i<advanceSessionPropertyCount;i++) {
			Map<Object,Object> advPropDetail=(Map)map.get("transformations.advancedProperties("+i+")");
			Object[] o=advPropDetail.keySet().toArray();
			for(int j=0;j<o.length;j++) {
				if(advPropDetail.get("advancedProperties.name").equals(propertyName))
					return (String)advPropDetail.get("advancedProperties.value");
			}
		}
		return null;
	}
	
	protected void trace(String transformationName) {
		System.out.println("Validating "+ transformationType +" - " + transformationName);
	}
	
	public Map<String, String> getValidationResults(){
		return transformationValidationResults;
	}
	
}
