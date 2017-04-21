package com.data;

public class PODataList {
	
	private String predicate;
	private String object;
	
	
	public PODataList(String predicate,String object)
	{
		this.predicate=predicate;
		this.object=object;
	}
	
	public String getPredicate()
	{
		return predicate;
	}
	public String getObject()
	{
		return object;
	}

}
