package com.wow.dev.mc;

import java.util.LinkedHashMap;
import java.util.Map;
import com.wow.dev.controller.ExtractJSONDetails;

public class Mapping{
	
	private Map<Object, Object> map;
	private Map<String, String> mappingValidationResults;
	private Map<Object, Object> template;
	private ExtractJSONDetails jsonDetails;
	private String mappingName;
	
	
	
	public Mapping(Map<Object, Object> map, Map<Object, Object> template, ExtractJSONDetails jsonDetails) {
		this.map=map;
		this.template=template;
		this.jsonDetails=jsonDetails;
		mappingValidationResults=new LinkedHashMap<String, String>();
	}
	
	
	public boolean  validateMappingName(Map<Object,Object> validationList,int i) {
		this.mappingName=(String)((Map)map.get("root.content")).get("content.name");
		if(!mappingName.substring(0, 2).equalsIgnoreCase("m_")) {
			validationList.put(i+"_MAPPING.NAME","Invalid Start of Mapping Name [" + mappingName + "]. Mapping Name Should Start with 'M_'");
			return false;
		}
		return true;
	}
	
	public boolean  validateMappingDescription(Map<Object,Object> validationList,int i) {
		String mappingDescription=getMappingParameterDetails("content.annotations","annotations.body");
		if(mappingDescription==null || mappingDescription.isEmpty()) {
			validationList.put(i+"_MAPPING.DESCRIPTION","Mapping [" + mappingName + "] description is empty. Add relevant description to the mapping.");
			return false;
		}
		return true;
	}
	
	public int getMappingParameterCount() {
		return jsonDetails.getChildCount(((Map)map.get("root.content")), "parameters.name");
	}
	
	public int getMappingTransformationCount() {
		return jsonDetails.getChildCount(map, "transformations");
	}
	
	

	public String  getMappingParameterDetails(String nodeType, String nodeName) {
		String list="";
		Map<Object,Object> nodeDetails=((Map)map.get("root.content"));
		Object[] o=nodeDetails.keySet().toArray();
		for(int i=0;i<o.length;i++) {
			if(o[i].toString().contains(nodeType)) {
				Map<Object,Object> childDetails=((Map) nodeDetails.get(o[i]));
				if (list=="") {
					if(childDetails.get(nodeName)!=null)
						list=(String)childDetails.get(nodeName);
				}
				else {
					if(childDetails.get(nodeName)!=null)
						list+=", "+(String)childDetails.get(nodeName);
				} 
			}
		}
		
		return list;
	}
	
	public boolean validateMappingisValid(Map<Object,Object> validationList,int i) {
		
		String isValid=(String)template.get("root.valid");
		if(!isValid.equalsIgnoreCase("true")) {
			validationList.put(i+"_MAPPING.ISVALID","Mapping [" + mappingName + "] is not Valid. .");
			return false;
		}
		return true;
		
	}
	
	public boolean validateMappingHasFixedCon() {
		String hasFixedCon=(String)template.get("root.fixedConnection");
		return Boolean.parseBoolean(hasFixedCon);
	}
	
	public int validateTotalMappingTask() {
		String totalMappingTask=(String)template.get("root.tasks");
		return Integer.parseInt(totalMappingTask);
	}
	
	public void validate(Map<Object,Object> validationList,int i) {
		mappingValidationResults.put("MAPPING_NAME_VALIDATION", validateMappingName(validationList,i)?"PASS":"FAIL");
		System.out.println("Validating Mapping - " + mappingName);
		mappingValidationResults.put("MAPPING_DESCRIPTION_VALIDATION", validateMappingDescription(validationList,i)?"PASS":"WARNING");
		mappingValidationResults.put("MAPPING_TRANSFORMATION_COUNT_VALIDATION", Integer.toString(getMappingTransformationCount()));
		mappingValidationResults.put("MAPPING_PARAMETER_COUNT_VALIDATION", Integer.toString(getMappingParameterCount()));
		mappingValidationResults.put("MAPPING_TRANSFORMATION_LIST_VALIDATION", getMappingParameterDetails("content.transformations","transformations.name"));
		mappingValidationResults.put("MAPPING_PARAMETER_LIST_VALIDATION", getMappingParameterDetails("content.parameters","parameters.name"));
		mappingValidationResults.put("MAPPING_IS_VALID", validateMappingisValid(validationList,i)?"PASS":"FAIL");
		mappingValidationResults.put("MAPPING_HAS_FIXED_CON_VALIDATION", validateMappingHasFixedCon()?"YES":"NO");
		mappingValidationResults.put("MAPPING_TASK_COUNT_VALIDATION", Integer.toString(validateTotalMappingTask()));
	}


	public String getMappingName() {
		return mappingName;
	}
	
	public Map<String,String> getValidationResults(){
		return mappingValidationResults;
	}

}
