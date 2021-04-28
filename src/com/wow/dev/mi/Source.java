package com.wow.dev.mi;

import java.util.Map;

public class Source {
	
	private String sourceType;
	private String sourceConnectionName;
	private String filePatternType;
	private String sourceFilePattern;
	private String sourceDirectory;
	private String includeSubfolder;
	private String checkDuplicate;
	private String batchSize;
	private String postPickupAction;
	private String fileDateFilter;
	
	public Source(Map<Object,Object> sourceConnection, Map<Object,Object> sourceParameter) {
//		this.sourceType=(String) sourceConnection.get("sourceConnection.type");
//		this.sourceConnectionName=(String) sourceConnection.get("sourceConnection.name");
//		this.filePatternType=(String) sourceParameter.get("sourceParameters.filePatternType");
//		this.sourceFilePattern=(String) sourceParameter.get("sourceParameters.filePattern");
//		this.sourceDirectory=(String) sourceParameter.get("sourceParameters.sourceDirectory");
//		this.includeSubfolder=(String) sourceParameter.get("sourceParameters.includeSubfolder");
//		this.checkDuplicate=(String) sourceParameter.get("sourceParameters.checkDuplicate");
//		this.batchSize=(String) sourceParameter.get("sourceParameters.batchSize");
//		this.postPickupAction=(String) sourceParameter.get("sourceParameters.postPickupAction");
		
		this.sourceType=(String) sourceConnection.get("sourceConnection.type");
		if(sourceType.contentEquals("local")) {
			this.sourceConnectionName="Local Folder";
		}else {
			this.sourceConnectionName=(String) sourceConnection.get("sourceConnection.name");
		}
		
		
		this.filePatternType=(String) sourceParameter.get("sourceOptions.src.file.pattern.type");
		this.sourceFilePattern=(String) sourceParameter.get("sourceOptions.src.file.pattern");
		
		if(sourceType.contentEquals("gcs")) {
			sourceDirectory=(String) sourceParameter.get("sourceOptions.src.download.path");
		} else if(sourceType.contentEquals("AmazonS3")) {
			sourceDirectory=(String) sourceParameter.get("sourceOptions.s3SourceLocation");
		}else if(sourceType.contentEquals("Advanced SFTP V2")) {
			sourceDirectory=(String) sourceParameter.get("sourceOptions.src.download.path");
		}else if(sourceType.contentEquals("local")) {
			sourceDirectory=(String) sourceParameter.get("sourceOptions.src.download.path");
		}else if(sourceType.contentEquals("Azure Blob")) {
			sourceDirectory=(String) sourceParameter.get("sourceOptions.blobSourceLocation");
		}
		
		this.batchSize=(String) sourceParameter.get("sourceOptions.batchSize");
		this.postPickupAction=(String) sourceParameter.get("sourceOptions.src.file.delete");
		this.includeSubfolder=(String) sourceParameter.get("sourceOptions.src.dir.subfolders");
		this.checkDuplicate=(String) sourceParameter.get("sourceOptions.src.file.deduplicate");
		if((String) sourceParameter.get("sourceOptions.src.file.fileDateFilter")=="true") {
			fileDateFilter=((String) sourceParameter.get("sourceOptions.relativeDays") + " " + ((String) sourceParameter.get("sourceOptions.dateFilterType")));
		}
		
	}
	
	
	public String getSourceType() {
		return sourceType;
	}

	public String getSourceConnectionName() {
		return sourceConnectionName;
	}

	public String getFilePatternType() {
		return filePatternType;
	}

	public String getSourceFilePattern() {
		return sourceFilePattern;
	}

	public String getSourceDirectory() {
		return sourceDirectory;
	}

	public String getIncludeSubfolder() {
		return includeSubfolder;
	}

	public String getCheckDuplicate() {
		return checkDuplicate;
	}

	public String getBatchSize() {
		return batchSize;
	}

	public String getPostPickupAction() {
		return postPickupAction;
	}
	
	public String getFileDateFilter() {
		return fileDateFilter;
	}
	
	@Override
	public String toString() {

		return "["+sourceType+", "+sourceConnectionName+", "+filePatternType+", "+sourceFilePattern+", "+sourceDirectory+", "+includeSubfolder+", "+checkDuplicate+", "+batchSize+", "+postPickupAction+", "+fileDateFilter+"]";
	}

}
