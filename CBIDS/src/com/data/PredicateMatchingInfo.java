package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class PredicateMatchingInfo {
	/**
	 * Save Link-Policy mating Information
	 * 
	 * sourcePredicate: source Entity's matching predicate;
	 * targetPredicate: target Entity's matching preciate
	 * objectList: save matching source predicate's object
	 * 
	 * scoreList: save objectComparison results
	 * 
	 * sourcePredicateflag: 0-> objectComparison not over 1-> objectComparison over
	 * targetPredicateFlag: 0-> objectComparison not over 1-> objectComparison over
	 */

	//private String sourcePredicate; //sourceLOD's predicate registered in link-Policy
	private Hashtable<String,String> sourcePredicate;
	private Hashtable<String,String> targetPredicateList;//targetLOD's predicate matched with sourceLOD's predicate
	private ArrayList<String> objectList;//sourcePredicate's objectList
	
	private List<Double> scoreList; //save target entity's similarity scores
	
	private int sourcePredicateFlag;
	private int targetPredicateFlag;
	
	private int predicateType;
	
	public PredicateMatchingInfo(String sourcePredicate,String sourceObject,String targetPredicate)
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		this.sourcePredicate=new Hashtable<String,String>();
		this.sourcePredicate.put(sourcePredicate,sourcePredicate);
		this.targetPredicateList.put(targetPredicate,targetPredicate);
		this.objectList.add(sourceObject);
		
		this.sourcePredicateFlag=0;
		this.predicateType=0;
		this.targetPredicateFlag=0;
	}
	
	public PredicateMatchingInfo(String sourcePredicate,String targetPredicate)
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		this.sourcePredicate=new Hashtable<String,String>();
		this.sourcePredicate.put(sourcePredicate,sourcePredicate);
		this.targetPredicateList.put(targetPredicate,targetPredicate);
		this.sourcePredicateFlag=0;
		this.targetPredicateFlag=0;
	}
	
	public PredicateMatchingInfo(String sourcePredicate,String targetPredicate,int predicatetype)
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		this.sourcePredicate=new Hashtable<String,String>();
		this.sourcePredicate.put(sourcePredicate,sourcePredicate);
		this.targetPredicateList.put(targetPredicate,targetPredicate);
		this.sourcePredicateFlag=0;
		this.targetPredicateFlag=0;
		this.predicateType=predicatetype;
	}
	
	public PredicateMatchingInfo()
	{
		this.targetPredicateList=new Hashtable<String,String>();
		this.objectList=new ArrayList<String>();
		
		this.sourcePredicateFlag=0;
		this.targetPredicateFlag=0;
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
		sourcePredicateFlag=1;
	}
	
	public int getSourcePredicateflag()
	{
		return sourcePredicateFlag;
	}
	
	public void setPredicateType()
	{
		this.predicateType=1;
	}
	
	public int getPredicateType()	
	{
		return this.predicateType;
	}
	
	public void setTargetPredicateflag(int num)
	{
		targetPredicateFlag=num;
	}
	
	public int getTagetPredicateflag()
	{
		return targetPredicateFlag;
	}
	
	
	public void addSourcePredicate(String sourcePredicate)
	{
		this.targetPredicateList.put(sourcePredicate,sourcePredicate);
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
	public Hashtable<String,String> getSourcePredicate()
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
