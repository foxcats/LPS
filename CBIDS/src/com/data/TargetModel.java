package com.data;

import org.apache.jena.rdf.model.Model;

public class TargetModel {
	/**
	 * Target entities that is searched by LPS 
	 * They will be used for ObjectComparison
	 * 
	 * targetType: target LOD's type that is registered in Link-Policy
	 */
	private String targetType;
	private Model targetCandidateModel;
	private String targetLOD;
	
	
	public TargetModel( String targetType, Model targetCandiateModel,String targetLOD)
	{
		//this.sourceType=sourceType;
		this.targetType=targetType;
		this.targetCandidateModel=targetCandiateModel;
		this.targetLOD=targetLOD;
	}

	public String getTargetType() {
		return targetType;
	}


	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}


	public Model getTargetCandidateModel() {
		return targetCandidateModel;
	}


	public void setTargetCandidateModel(Model targetCandidateModel) {
		this.targetCandidateModel = targetCandidateModel;
	}

	public void setTargetLOD(String targetLOD)
	{
		this.targetLOD=targetLOD;
	}
	
	public String getTargetLOD()
	{
		return this.targetLOD;
	}
}
