package com.storage;

import java.util.ArrayList;
import java.util.Hashtable;

import com.data.EC;

public class DepthCollector {

	private Hashtable<Integer,ArrayList<EC>> depthConfidenceList;
	
	private ArrayList<EC> confidenceList;
	
	
	
	public DepthCollector()
	{
		this.depthConfidenceList=new Hashtable<Integer,ArrayList<EC>>();
		this.confidenceList=new ArrayList<EC>();
	}
	
	public void insertEC(int depth, double ec, int count)
	{
		if(this.confidenceList.isEmpty())
		{
			this.confidenceList.add(new EC(count,ec));
		}
		else
		{
			EC currentEC=this.confidenceList.get(this.confidenceList.size()-1);
			
			if(currentEC.getEc()<ec)
			{
				this.confidenceList.add(new EC(count,ec));
			}
		}
		
		
		ArrayList<EC> depthlist=this.depthConfidenceList.get(depth);
		
		if(depthlist==null)
		{
			depthlist=new ArrayList<EC>();
			depthlist.add(new EC(count,ec));
			
			this.depthConfidenceList.put(depth,depthlist);
		}
		else
		{
			EC currentEC2=depthlist.get(depthlist.size()-1);
			
			if(currentEC2.getEc()<ec)
			{
				depthlist.add(new EC(count,ec));
			}
		}
		
		
	}

	public ArrayList<EC> getConfidenceList() {
		return confidenceList;
	}

	public void setConfidenceList(ArrayList<EC> confidenceList) {
		this.confidenceList = confidenceList;
	}
	
}
