package com.wow.dev.mc;

import java.util.LinkedHashMap;
import java.util.Map;

import com.wow.dev.controller.ExtractJSONDetails;

public class MappingConfig {
	private String mcName;
	private Map<Object,Object> map;
	private Map<String, String> mappingConfigValidationResults;
	private ExtractJSONDetails jsonDetails;
	private String folderName;
	
	public MappingConfig(Map<Object, Object> map,ExtractJSONDetails jsonDetails,String folderName) {
		this.map=map;
		this.jsonDetails=jsonDetails;
		this.folderName=folderName;
		mappingConfigValidationResults=new LinkedHashMap<String, String>();
	}
	
	public boolean  validateMappingConfigNameValidation(Map<Object,Object> validationList,int i) {
		this.mcName=(String)map.get("mc.name");
		if(!mcName.substring(0, 3).equalsIgnoreCase("mc_")) {
			validationList.put(i+"_MAPPINGCONFIG.NAME","Invalid Start of Mapping Config Name [" + mcName + "]. Mapping Config Name Should Start with 'MC_'");
			return false;
		}
		return true;
	}
	
	public boolean  validateMappingConfigDescription(Map<Object,Object> validationList,int i) {
		String description=(String)map.get("mc.description");
		if(description.isEmpty()) {
			validationList.put(i+"_MAPPINGCONFIG.DESCRIPTION","Mapping Config [" + mcName + "] description is empty. Add relevant description to mapping config.");
			return false;
		}
		return true;
	}
	
	public String getRunTimeEnvironment() {
		return (String)map.get("mc.runtimeEnvironmentId");
	}
	
	public boolean verboseValidation(Map<Object,Object> validationList,int i) {
		String verbose=(String)map.get("mc.verbose");
		if(verbose.equalsIgnoreCase("true")) {
			validationList.put(i+"_MAPPINGCONFIG.VERBOSE","Mapping Config [" + mcName + "] Verbose Mode is set to "+verbose+". Check the verbose property in the Mapping Config");
			return false;
		}
		return true;
	}
	
	public String getMappingID() {
		return (String)map.get("mc.mappingId");
	}
	
	public String getParameterFileName() {
		return (String)map.get("mc.parameterFileName");
	}
	
	public String getParamFileType() {
		return (String)map.get("mc.paramFileType");
	}
	
	public String getParameterFileDir() {
		return (String)map.get("mc.parameterFileDir");
	}
	
	private boolean validateParamFileDir(Map<Object, Object> validationList, int i) {
		if(getParameterFileDir()!=null && !getParameterFileDir().contains(folderName)) {
			validationList.put(i+"_MAPPINGCONFIG.PARAM_FILE_DIR","Mapping Config [" + mcName + "] Param Directory "+getParameterFileDir()+"Should be pointint to "+folderName);
			return false;
		}
		return true;
	}
	
	public Map<Object,Object> getAdvanceSessionProperties() {
		Map<Object,Object> sessionPropertyDetails = new LinkedHashMap<Object, Object>();
		int advanceSessionPropertyCount= jsonDetails.getParentCount(map, "mc.sessionPropertiesList");
		for(int i=0;i<advanceSessionPropertyCount;i++) {
			Map<Object,Object> sessPropDetail=(Map)map.get("mc.sessionPropertiesList("+i+")");
			if(sessPropDetail!=null) {
				Object[] o=sessPropDetail.keySet().toArray();
				for(int j=0;j<o.length;j++) {
					sessionPropertyDetails.put("SessionProperty."+sessPropDetail.get("sessionPropertiesList.name"), sessPropDetail.get("sessionPropertiesList.value"));
				}
			}
		}
		return sessionPropertyDetails;
	}
	
	public boolean logFileNameValidation(Map<Object,Object> sessPropDetail,Map<Object, Object> validationList, int i) {
		String logFileName=(String) sessPropDetail.get("SessionProperty.Session Log File Name");
		if(logFileName==null || !(logFileName.equalsIgnoreCase(mcName+".log"))) {
			validationList.put(i+"_MAPPINGCONFIG.SESSION_LOG_FILE_NAME","Mapping Config [" + mcName + "] Log File Name ("+logFileName+") should be same as mapping config name");
			return false;
		}
		return true;
	}
	
	public boolean logFileDirValidation(Map<Object,Object> sessPropDetail,Map<Object, Object> validationList, int i) {
		String logFileDir=(String) sessPropDetail.get("SessionProperty.Session Log File directory");
		if(logFileDir==null || !(logFileDir.contains(folderName))) {
			validationList.put(i+"_MAPPINGCONFIG.SESSION_LOG_DIR","Mapping Config [" + mcName + "] Log File directory ("+logFileDir+") should be pointing to "+folderName);
			return false;
		}
		return true;
	}
	
	public boolean stopOnErrosValidation(Map<Object,Object> sessPropDetail,Map<Object, Object> validationList, int i) {
		String stopOnErros=(String) sessPropDetail.get("SessionProperty.Stop on errors");
		if(stopOnErros==null) {
			validationList.put(i+"_MAPPINGCONFIG.SESSION_STOP_ON_ERRORS","Mapping Config [" + mcName + "] Stop On Errors Property is not enabled at Mapping Config Session Property");
			return false;
		}else {
			int stopOnErrosValue=Integer.parseInt(stopOnErros);
			if(stopOnErrosValue==0) {
				validationList.put(i+"_MAPPINGCONFIG.SESSION_STOP_ON_ERRORS","Mapping Config [" + mcName + "] Stop On Errors is set to 0");
				return false;
			}
		}
		return true;
	}
	
	public boolean backwardCompatibleValidation(Map<Object,Object> sessPropDetail,Map<Object, Object> validationList, int i) {
		String backwardCompatible=(String) sessPropDetail.get("SessionProperty.Write Backward Compatible Session Log File");
		if(backwardCompatible==null || !(backwardCompatible.equalsIgnoreCase("YES"))) {
			validationList.put(i+"_MAPPINGCONFIG.SESSION_BACKWARD_COMPATIBLE_LOG","Mapping Config [" + mcName + "] Backward Compatible Session Log File at Mapping Config Property is set to "+backwardCompatible);
			return false;
		}
		return true;
	}
	
	
	public void validate(Map<Object,Object> validationList,int i) {
		mappingConfigValidationResults.put("MAPPING_CONFIG_NAME_VALIDATION", validateMappingConfigNameValidation(validationList,i)?"PASS":"FAIL");
		System.out.println("Validation Mapping Config - " + mcName);
		mappingConfigValidationResults.put("MAPPING_CONFIG_DESCRIPTION_VALIDATION", validateMappingConfigDescription(validationList,i)?"PASS":"WARNING");
		mappingConfigValidationResults.put("MAPPING_CONFIG_RUNTIME_VALIDATION", getRunTimeEnvironment());
		mappingConfigValidationResults.put("MAPPING_CONFIG_TRACING_LEVEL_VALIDATION", verboseValidation(validationList,i)?"PASS":"FAIL");
		mappingConfigValidationResults.put("MAPPING_CONFIG_PARAM_FILE", getParameterFileName());
		mappingConfigValidationResults.put("MAPPING_CONFIG_PARAM_FILE_VALIDATION", getParameterFileDir()+"/"+getParameterFileName());
//		mappingConfigValidationResults.put("MAPPING_CONFIG_PARAM_FILE_TYPE_VALIDATION", getParamFileType());
		mappingConfigValidationResults.put("MAPPING_CONFIG_PARAM_FILE_DIR_VALIDATION", validateParamFileDir(validationList,i)?"PASS":"FAIL");
		Map<Object,Object> SessionProp=getAdvanceSessionProperties();
		mappingConfigValidationResults.put("MAPPING_CONFIG_LOG_FILE_VALIDATION", logFileNameValidation(SessionProp,validationList,i)?"PASS":"FAIL");
		mappingConfigValidationResults.put("MAPPING_CONFIG_LOG_FILE_DIR_VALIDATION", logFileDirValidation(SessionProp,validationList,i)?"PASS":"WARNING");
		mappingConfigValidationResults.put("MAPPING_CONFIG_STOP_ON_ERROS_VALIDATION", stopOnErrosValidation(SessionProp,validationList,i)?"PASS":"WARNING");
		mappingConfigValidationResults.put("MAPPING_CONFIG_BACKWARD_COMPATIBLE_VALIDATION", backwardCompatibleValidation(SessionProp,validationList,i)?"PASS":"FAIL");
	}

	public String getMcName() {
		return mcName;
	}
	
	public Map<String,String> getValidationResults(){
		return mappingConfigValidationResults;
	}

}
