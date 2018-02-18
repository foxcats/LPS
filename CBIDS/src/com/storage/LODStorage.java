package com.storage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.data.Entity;
import com.lod.LOD;

public class LODStorage {
	
	
	private Hashtable<String,LOD> lodList;
	
	public LODStorage()
	{
		this.lodList=new Hashtable<String,LOD>();
	}

	public Hashtable<String, LOD> getLodList() {
		return lodList;
	}

	public void setLodList(Hashtable<String, LOD> lodList) {
		this.lodList = lodList;
	}
	
	public LOD getLC(Entity source)
	{
		
		LOD lod=lodList.get(source.getSparqlEndPoint());
		
		if(lod==null)
		{
			lod= new LOD(source.getSparqlEndPoint());
			lod.setLodConfidence((Math.random()*0.3)+0.4);
			this.lodList.put(source.getSparqlEndPoint(), lod);
			
			return lod;
		}
		
		return lod;
	}
	
	public String makeSparqlEndpoint(String tmp)
	{
		//Make SPARQL endpoint by uri address
		
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		StringTokenizer st = new StringTokenizer(tmp,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(0)+"//"+tmp_str.get(1)+"/sparql";

	}
	

	
}
