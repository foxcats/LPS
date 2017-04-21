package com.linkpolicy;

import java.util.ArrayList;
import java.util.HashMap;

import com.data.TargetPredicate;

public class LinkPolicy {

	
	
	private String typePredicate;
	private ArrayList<String> typeRestriction;
	private ArrayList<String> sourcePredicate;
	private HashMap<String,TargetPredicate> targetPredicate;

	public LinkPolicy()
	{
		this.typeRestriction=new ArrayList<String>();
		this.sourcePredicate=new ArrayList<String>();
		this.targetPredicate=new HashMap<String,TargetPredicate>();
	}
	
	public String getTypePredicate() {
		return typePredicate;
	}
	public void setTypePredicate(String typePredicate) {
		this.typePredicate = typePredicate;
	}
	public ArrayList<String> getTypeRestriction() {
		return typeRestriction;
	}
	public void setTypeRestriction(ArrayList<String> typeRestriction) {
		this.typeRestriction = typeRestriction;
	}
	public ArrayList<String> getSourcePredicate() {
		return sourcePredicate;
	}
	public void setSourcePredicate(ArrayList<String> sourcePredicate) {
		this.sourcePredicate = sourcePredicate;
	}
	public HashMap<String, TargetPredicate> getTargetPredicate() {
		return targetPredicate;
	}
	public void setTargetPredicate(HashMap<String, TargetPredicate> targetPredicate) {
		this.targetPredicate = targetPredicate;
	}
	
	
}
