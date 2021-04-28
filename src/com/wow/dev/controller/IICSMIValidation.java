package com.wow.dev.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wow.dev.mi.MassIngestion;

public class IICSMIValidation {

	private Map<Object, Object> validationList;
	private MassIngestion mi;
	private String massIngestionFlow;
	private Map<String, String> miValidationResults;
	
	// Validation of Results
	private int TOTAL_TEST_CASE_COUNT;
	private int PASS_CASE_COUNT;
	private int FAIL_CASE_COUNT;
	private int WARNING_CASE_COUNT;
	
	public IICSMIValidation(String jsonMassIngestionValue,Map<Object, Object> validationList,String folderName) {
		ExtractJSONDetails jsonDetails = new ExtractJSONDetails();
		Map<Object, Object> jsonMapDetails=jsonDetails.extractDataFromJSON("root", jsonMassIngestionValue);
		
		this.validationList=validationList;
		this.mi = new MassIngestion(jsonMapDetails,jsonDetails,folderName);
		miValidationResults=new LinkedHashMap<String, String>();
	}
	
	public String getMassIngestionFlow() {
		return mi.getSource().getSourceType()+" to "+mi.getTarget().getTargetType();
	}
	
	public boolean  validateMassIngestionName() {
		if(!mi.getMassIngestionName().substring(0, 3).equalsIgnoreCase("MI_")) {
			validationList.put("MASSINGESTION.NAME","Invalid Start of Massingestion [" + mi.getMassIngestionName() + "]. Name Should Start with 'MI'");
			return false;
		}
		return true;
	}
	
	
	public boolean  validateMassIngestionDescription() {
		if(mi.getMassIngestiondescription().isEmpty()) {
			validationList.put("MASSINGESTION.DESCRIPTION","Massingestion [" + mi.getMassIngestionName() + "] description is empty. Add a valid description to the MI");
			return false;
		}
		return true;
	}
	
	
	public boolean  validateMassIngestionTracing() {
		if(!mi.getMassIngestionlogLevel().contentEquals("NORMAL")) {
			validationList.put("MASSINGESTION.TRACINGLOG","Massingestion [" + mi.getMassIngestionName() + "] Tracing Log is set to -"+mi.getMassIngestionlogLevel()+". Check the tracing log option.");
			return false;
		}
		return true;
	}
	
	public boolean  validateMISourcePattern() {
		if(mi.getSource().getSourceFilePattern().isEmpty()) {
			validationList.put("MASSINGESTION.SOURCE PATTERN","Massingestion [" + mi.getMassIngestionName() + "] Source Pattern is empty. Verify the Source Pattern");
			return false;
		}
		return true;
	}
	
	public boolean  validateMISourceDirectory() {
		if(mi.getSource().getSourceDirectory().isEmpty()) {
			validationList.put("MASSINGESTION.SOURCE DIRECTORY","Massingestion [" + mi.getMassIngestionName() + "] Source Directory is empty. Source Directory cannot be empty");
			return false;
		}
		return true;
	}
	
	public boolean  validateMITargetDirectory() {
		if(mi.getTarget().getTargetDirectory().isEmpty()) {
			validationList.put("MASSINGESTION.TARGET DIRECTORY","Massingestion [" + mi.getMassIngestionName() + "] Target Directory is empty. Target Directory cannot be empty");
			return false;
		}
		return true;
	}
	

	public void validateMassIngestion() {
		massIngestionFlow=getMassIngestionFlow();
		miValidationResults.put("MI_NAME_VALIDATION", validateMassIngestionName()?"PASS":"FAIL");
		miValidationResults.put("MI_DESCRIPTION_VALIDATION", validateMassIngestionDescription()?"PASS":"WARNING");
		miValidationResults.put("MI_TRACING_VALIDATION", validateMassIngestionTracing()?"PASS":"FAIL");
		miValidationResults.put("MI_SRC_PTRN_VALIDATION", validateMISourcePattern()?"PASS":"FAIL");
		miValidationResults.put("MI_SRC_DIR_VALIDATION", validateMISourceDirectory()?"PASS":"FAIL");
		miValidationResults.put("MI_TGT_DIR_VALIDATION", validateMITargetDirectory()?"PASS":"FAIL");
	}
	
	public void validateTestCase() {
		Iterator<String> it=miValidationResults.values().iterator();
		while(it.hasNext()) {
			String value=it.next();
			if(value.equalsIgnoreCase("FAIL")) {
				FAIL_CASE_COUNT++;
			}else if(value.equalsIgnoreCase("WARNING")) {
				WARNING_CASE_COUNT++;
			}else {
				PASS_CASE_COUNT++;
			}
			
			TOTAL_TEST_CASE_COUNT++;
		}
	}
	
	public void createTestSummaryReport() {
		
		validateTestCase();

		try {
			File report=new File("Informatica_code_review_test_summary_report.txt");
			if(!report.exists()) {report.createNewFile();}
			String testSummary=mi.getMassIngestionName() + " - [TOTAL TEST CASE : " + TOTAL_TEST_CASE_COUNT + ", PASS : " + PASS_CASE_COUNT + ", FAIL : " + FAIL_CASE_COUNT + ", WARNING : " + WARNING_CASE_COUNT + "]\n";
		    Files.write(Paths.get("Informatica_code_review_test_summary_report.txt"), testSummary.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public MassIngestion getMi() {
		return mi;
	}
	
	public String getmassIngestionFlow() {
		return massIngestionFlow;
	}

	public Map<String,String> getValidationResults() {
		return miValidationResults;
	}

	public int getTOTAL_TEST_CASE_COUNT() {
		return TOTAL_TEST_CASE_COUNT;
	}

	public int getPASS_CASE_COUNT() {
		return PASS_CASE_COUNT;
	}

	public int getFAIL_CASE_COUNT() {
		return FAIL_CASE_COUNT;
	}

	public int getWARNING_CASE_COUNT() {
		return WARNING_CASE_COUNT;
	}
	
	
	
}
