package com.data;

import java.util.ArrayList;

public class TargetPredicate {
	
	
	private ArrayList<String> targetList;
	
	public TargetPredicate()
	{
		this.targetList=new ArrayList<String>();
	}
	
	
	
	public void addTargetPredicate(String targetPredicate)
	{
		targetList.add(targetPredicate);
	}
	
	public ArrayList<String> getTargetList()
	{
		return this.targetList;
	}

}
