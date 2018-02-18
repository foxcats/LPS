package com.lod;

import java.util.Hashtable;

import com.data.Entity;

public class LOD {

	private Hashtable<Integer, Entity> entityList;
	private double lodConfidence;
	private int lodN;
	private String LOD;
	
	public LOD(String LOD)
	{
		this.LOD=LOD;
		this.entityList=new Hashtable<Integer, Entity>();
		this.lodConfidence=0;
		this.lodN=0;
	}
	
	public String getLOD() {
		return LOD;
	}


	public void setLOD(String lOD) {
		LOD = lOD;
	}


	public Hashtable<Integer, Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(Hashtable<Integer, Entity> entityList) {
		this.entityList = entityList;
	}

	public double getLodConfidence() {
		return lodConfidence;
	}

	public void setLodConfidence(double lodConfidence) {
		this.lodConfidence = lodConfidence;
	}

	public int getLodN() {
		return lodN;
	}

	public void setLodN(int lodN) {
		this.lodN = lodN;
	}
	
}
