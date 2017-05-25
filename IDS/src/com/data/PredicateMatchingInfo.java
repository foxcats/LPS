package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class PredicateMatchingInfo {

	private String sourcePredicate; //sourceLOD's predicate registered in link-Policy
	private Hashtable<String,String> targetPredicateList;//targetLOD's predicate matched with sourceLOD's predicate
	private ArrayList<String> objectList;//sourcePredicate's objectList
	
	private List<Double> scoreList; //save target entity's similarity scores
	
	private int sourcePredicateflag;
	private int targetPredicateFalg;
	
	
	
	public PredicateMatchingInfo(String sourcePredicate,String sourceObject,String targetPredicate)
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		this.sourcePredicate=sourcePredicate;
		this.targetPredicateList.put(targetPredicate,targetPredicate);
		this.objectList.add(sourceObject);
		
		this.sourcePredicateflag=0;
		this.targetPredicateFalg=0;
	}
	
	
	
	public PredicateMatchingInfo()
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		
		this.sourcePredicateflag=0;
		this.targetPredicateFalg=0;
	}
	
	
	public void setScoreList()
	{
		this.scoreList=new ArrayList<Double>();
	}
	
	public boolean checkObjectList()
	{
		if(objectList.isEmpty())
			return false;
		
		return true;
	}

	public void setSourcePredicateflag()
	{
		sourcePredicateflag=1;
	}
	
	public int getSourcePredicateflag()
	{
		return sourcePredicateflag;
	}
	
	public void setTargetPredicateflag(int num)
	{
		targetPredicateFalg=num;
	}
	
	public int getTagetPredicateflag()
	{
		return targetPredicateFalg;
	}
	
	
	public void addSourcePredicate(String sourcePredicate)
	{
		this.sourcePredicate=sourcePredicate;
	}
	
	public void addTargetPredicate(String targetPredicate)
	{
		targetPredicateList.put(targetPredicate,targetPredicate);
	}
	
	public void addObject(String object)
	{
		this.objectList.add(object);
	}
	
	public ArrayList<String>getObjectList()
	{
		return this.objectList;
	}
	public String getSourcePredicate()
	{
		return this.sourcePredicate;
	}
	
	public Hashtable<String,String> getTargetPredicateList()
	{
		return this.targetPredicateList;
	}
	
	public List<Double> getScoreList()
	{
		return scoreList;
	}

	
}
