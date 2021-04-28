package com.wow.dev.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Map;

import com.wow.dev.mc.Aggregator;
import com.wow.dev.mc.Expression;
import com.wow.dev.mc.Joiner;
import com.wow.dev.mc.Lookup;
import com.wow.dev.mc.Mapping;
import com.wow.dev.mc.MappingConfig;
import com.wow.dev.mc.Router;
import com.wow.dev.mc.Sorter;
import com.wow.dev.mc.Transformation;
import com.wow.dev.mc.Source;
import com.wow.dev.mc.Target;

public class IICSMCValidation {
	private String jsonMappingConfig;
	private String jsonMappingDetails;
	private String jsonMappingTemplate;
	private Map<Object,Object> validationList;
	private ExtractJSONDetails jsonDetails;
	private MappingConfig mappingConfig;
	private Mapping mapping;
	private Transformation transformation[];
	private String folderName;
	
	// Validation of Results
	private int TOTAL_TEST_CASE_COUNT;
	private int PASS_CASE_COUNT;
	private int FAIL_CASE_COUNT;
	private int WARNING_CASE_COUNT;

	
	
	public IICSMCValidation(String jsonMappingConfig,String jsonMappingDetails,String jsonMappingTemplate, String folderName, Map<Object,Object> validationList) {
		this.jsonMappingConfig=jsonMappingConfig;
		this.jsonMappingDetails=jsonMappingDetails;
		this.jsonMappingTemplate=jsonMappingTemplate;
		this.validationList=validationList;
		this.folderName=folderName;
		jsonDetails=new ExtractJSONDetails();
	}
	
	public void validateMappingConfig() {
		Map<Object,Object> mappingConfigDetails=mappingConfigDetails=jsonDetails.extractDataFromJSON("root", jsonMappingConfig);
		mappingConfig= new MappingConfig((Map)mappingConfigDetails.get("root.mc(0)"), jsonDetails, folderName);
		mappingConfig.validate(validationList, 0);
		validateTestCase(mappingConfig.getValidationResults());
	
		
		Map<Object,Object> mTemplateDetails=jsonDetails.extractDataFromJSON("root", jsonMappingDetails);
		
		Map<Object,Object> mappingTeamplate=jsonDetails.extractDataFromJSON("root", jsonMappingTemplate);
		mapping=new Mapping(mTemplateDetails, mappingTeamplate,jsonDetails);
		mapping.validate(validationList, 0);
		validateTestCase(mapping.getValidationResults());
		
		Map<Object,Object> metadataDetails=(Map)((Map)mTemplateDetails.get("root.metadata")).get("metadata.$$classInfo");
		
		int transformationCount=jsonDetails.getChildCount(mTemplateDetails, "content.transformations");
		transformation=new Transformation[transformationCount];
		for(int i=0;i<transformationCount;i++) {
			Map<Object,Object> transformationDetails= ((Map)((Map)mTemplateDetails.get("root.content")).get("content.transformations("+i+")"));
			String transMetadataClassID=(String)transformationDetails.get("transformations.$$class");
			String transMetadataClass=(String)metadataDetails.get("$$classInfo."+transMetadataClassID);
			
			switch(transMetadataClass) {
				case "com.informatica.metadata.template.tx.tmplsource.TmplSource":
					transformation[i]=new Source(transformationDetails, "Source",jsonDetails);
					break;
				case "com.informatica.metadata.template.tx.tmpljoiner.TmplJoiner":
					transformation[i]=new Joiner(transformationDetails, "Joiner",jsonDetails,folderName);
					break;
				case "com.informatica.metadata.template.tx.tmpllookup.TmplLookup":
					transformation[i]=new Lookup(transformationDetails, "Lookup",jsonDetails,folderName);
					break;
				case "com.informatica.metadata.template.tx.tmplexpression.TmplExpression":
					transformation[i]=new Expression(transformationDetails, "Expression",jsonDetails);
					break;
				case "com.informatica.metadata.template.tx.tmplsorter.TmplSorter":
					transformation[i]=new Sorter(transformationDetails, "Sorter",jsonDetails,folderName);
					break;
				case "com.informatica.metadata.template.tx.tmplaggregator.TmplAggregator":
					transformation[i]=new Aggregator(transformationDetails, "Aggregator",jsonDetails,folderName);
					break;
				case "com.informatica.metadata.template.tx.tmplrouter.TmplRouter":
					transformation[i]=new Router(transformationDetails, "Router",jsonDetails);
					break;
				case "com.informatica.metadata.template.tx.tmpltarget.TmplTarget":
					transformation[i]=new Target(transformationDetails, "Target",jsonDetails);
					break;
			}

			if(transformation[i]!=null) {
				transformation[i].validate(validationList, i);
				validateTestCase(transformation[i].getValidationResults());
			}
		}
	}
	
	
	public void validateTestCase(Map<String, String> validationResults) {

		Iterator<String> it=validationResults.values().iterator();
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

		try {
			File report=new File("Informatica_code_review_test_summary_report.txt");
			if(!report.exists()) {report.createNewFile();}
			String testSummary=mappingConfig.getMcName() + " - [TOTAL TEST CASE : " + TOTAL_TEST_CASE_COUNT + ", PASS : " + PASS_CASE_COUNT + ", FAIL : " + FAIL_CASE_COUNT + ", WARNING : " + WARNING_CASE_COUNT + "]\n";
		    Files.write(Paths.get("Informatica_code_review_test_summary_report.txt"), testSummary.getBytes(), StandardOpenOption.APPEND);
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	

	public MappingConfig getMappingConfig() {
		return mappingConfig;
	}

	public Mapping getMapping() {
		return mapping;
	}

	
	public Transformation[] getTransformation() {
		return transformation;
	}

	public String getFolderName() {
		return folderName;
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
