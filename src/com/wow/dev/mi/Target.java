package com.wow.dev.mi;

import java.util.Map;

public class Target {
	private String targetType;
	private String targetConnectionName;
	private String targetDirectory;
//	private String fileExistsAction;
//	private String compressionType;
	
	public Target(Map<Object,Object> targetConnection, Map<Object,Object> targetParameter) {
		this.targetType=(String) targetConnection.get("targetConnection.type");
		this.targetConnectionName=(String) targetConnection.get("targetConnection.name");
		
		
//		if(targetType.contentEquals("gcs")) {
//			targetDirectory=(String) targetParameter.get("targetParameters.gcsTargetLocation");
//		} else if(targetType.contentEquals("AmazonS3")) {
//			targetDirectory=(String) targetParameter.get("targetParameters.s3TargetLocation");
//		}else if(targetType.contentEquals("Advanced SFTP V2")) {
//			targetDirectory=(String) targetParameter.get("targetParameters.targetDirectory");
//		}else if(targetType.contentEquals("local")) {
//			targetDirectory=(String) targetParameter.get("targetParameters.targetDirectory");
//		}else if(targetType.contentEquals("Azure Blob")) {
//			targetDirectory=(String) targetParameter.get("targetParameters.blobContainer");
//		}
//		

//		this.fileExistsAction=(String) targetParameter.get("targetParameters.fileExistsAction");
//		this.compressionType=(String) targetParameter.get("targetParameters.compressionType");

		
		if(targetType.contentEquals("gcs")) {
			targetDirectory=(String) targetParameter.get("targetOptions.GCSTargetLocation");
		} else if(targetType.contentEquals("AmazonS3")) {
			targetDirectory=(String) targetParameter.get("targetOptions.s3Location");
		}else if(targetType.contentEquals("Advanced SFTP V2")) {
			targetDirectory=(String) targetParameter.get("targetOptions.tgt.download.path");
		}else if(targetType.contentEquals("local")) {
			targetDirectory=(String) targetParameter.get("targetOptions.tgt.download.path");
		}else if(targetType.contentEquals("Azure Blob")) {
			targetDirectory=(String) targetParameter.get("targetOptions.blobContainer");
		}
		
	}

	public String getTargetType() {
		return targetType;
	}

	public String getTargetConnectionName() {
		return targetConnectionName;
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

//	public String getFileExistsAction() {
//		return fileExistsAction;
//	}

//	public String getCompressionType() {
//		return compressionType;
//	}
	
	
	@Override
	public String toString() {
//		return "["+targetType+", "+targetConnectionName+", "+targetDirectory+", "+fileExistsAction+", "+compressionType+"]";
		return "["+targetType+", "+targetConnectionName+", "+targetDirectory+"]";
	}
}
