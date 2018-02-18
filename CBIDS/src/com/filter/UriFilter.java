package com.filter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.jena.query.ResultSet;

import com.controller.PESController;
import com.data.Entity;
import com.searcher.Searcher;

public class UriFilter {

	

	private Hashtable<String,String> preventList;
	private Hashtable<String,Integer> urilist;
	
	public UriFilter()	
	{
		this.preventList=new Hashtable<String,String>();
		this.urilist=new Hashtable<String,Integer>();
	}
	
	public void checkValidity(Entity source)
	{
		String sparql="select * {<"+source.getUri()+"> ?p ?o}";
		Searcher searcher=new Searcher();
		ResultSet sparqlResult=searcher.executeQuery(sparql, source.getSparqlEndPoint());

		if(sparqlResult==null)
		{
			source.setValidity(0);
		}
		else
		{
			source.setValidity(1);
		}
			
		/*
		try {
			URL url;
			url = new URL(uri);
			URLConnection con = url.openConnection();
			HttpURLConnection exitCode = (HttpURLConnection)con;
			exitCode.setConnectTimeout(10000);
			exitCode.setReadTimeout(10000);
			exitCode.getResponseCode();
			return true;
			
		} catch (IOException e) {
			return false;
		}*/
	}
	
	public boolean checkValidity(String uri,Entity source)
	{
		if(this.urilist.get(uri)==null){
			
			String sparql="select * {<"+uri+"> ?p ?o}";
			String sparqlEndPoint=makeSparqlEndpoint(uri);
			Searcher searcher=new Searcher();
			ResultSet sparqlResult=searcher.executeQuery(sparql, sparqlEndPoint);
			
			if(sparqlResult==null)
			{
				this.urilist.put(uri, 0);
				searcher.close();
				return false;
			}
			else
			{
				this.urilist.put(uri, 1);
				searcher.close();
				return true;
			}
			
		}else{
			
			if(this.urilist.get(uri)==0)
			{
				return false;
			}
			
			return true;
		}
		
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
	
	public boolean checkNode(Entity source)//check entity validity
	{
		
		if(source.getValidity()==-1)
		{
			checkValidity(source);
		}
		
		if(source.getValidity()==0)
		{
			return false;
		}
		
		return true;
	
	}
}
