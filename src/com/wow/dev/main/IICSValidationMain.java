package com.wow.dev.main;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;


import com.wow.dev.controller.ExtractJSONDetails;
import com.wow.dev.controller.IICSMCValidation;
import com.wow.dev.controller.IICSMIValidation;
import com.wow.dev.model.MCPDFResult;
import com.wow.dev.model.MIPDFResult;

public class IICSValidationMain {
	
	public static void main(String args[]) {

		try {
			
			String iicsObjectType = args[0];
			
//			Creation of Arraylist to catpure information, errors and warnings from the input XML
			Map<Object, Object> validationList=new LinkedHashMap<Object, Object>();
			
			
			if(iicsObjectType.equalsIgnoreCase("mi")) {
				String iicsObjectName = args[1];
				String peerReviewerName = args[2];
				String folderName=args[3];
				
				String jsonMassIngestionValue=readFile(iicsObjectName+".json",StandardCharsets.US_ASCII);
				
				IICSMIValidation miValidation = new IICSMIValidation(jsonMassIngestionValue, validationList,folderName);
				miValidation.validateMassIngestion();
				miValidation.createTestSummaryReport();
				System.gc();
				
				
				MIPDFResult pdfResult= new MIPDFResult();
				pdfResult.generateOutput(miValidation, validationList, peerReviewerName);				
			}else if(iicsObjectType.equalsIgnoreCase("mc")) {
				String iicsObjectName = args[1];
				String peerReviewerName=args[2];
				String iicsMappingObjectName=args[3];
				String folderName=args[4];
				
				String jsonMappingConfig=readFile(iicsObjectName+".json",StandardCharsets.US_ASCII);
				String jsonMappingTemplate=readFile(iicsMappingObjectName+"_TEMPLATE.json",StandardCharsets.US_ASCII);
				String jsonMappingDetails=readFile(iicsMappingObjectName+".json",StandardCharsets.US_ASCII);
				
				IICSMCValidation mcValidation=new IICSMCValidation(jsonMappingConfig,jsonMappingDetails,jsonMappingTemplate, folderName, validationList);
				mcValidation.validateMappingConfig();
				mcValidation.createTestSummaryReport();
				
				System.gc();
				
				MCPDFResult pdfResult= new MCPDFResult();
				pdfResult.generateOutput(mcValidation, validationList, peerReviewerName);				
				
				
			}

			System.gc();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	static String readFile(String path, Charset encoding) throws IOException
	{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
	}

}
