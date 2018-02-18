package com.sfs;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import com.data.Entity;
import com.searcher.Search;
import com.stringhandler.StringFunction;

public class SFS {
	
	private String query;
	private String sparqlEndPoint;
	private Search searcher;
	private StringFunction strFunc;
	public SFS(String query,String sparqlEndPoint,Search searcher)
	{
		this.query=query;
		this.sparqlEndPoint=sparqlEndPoint;
		this.searcher=searcher;
		this.strFunc=new StringFunction();
	}
	
	public ArrayList<Entity> searchSfs()
	{

		Hashtable<String,String> tmpList=new Hashtable<String,String>();
		String sparqlQuery=makeSparql(this.query);
		this.searcher.setQuery(sparqlQuery);
		this.searcher.setSparqlEndPoint(this.sparqlEndPoint);

		
		ArrayList<String> variableList=searchIntialURIList(sparqlQuery);
		ResultSet results=this.searcher.executeQuery(sparqlQuery, this.sparqlEndPoint);
		
		ArrayList<Entity> entityList=new ArrayList<Entity>();
		
		ArrayList<Entity> uriList=new ArrayList<Entity>();
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		       try{
		    	 
		    	   for(int i=0; i<variableList.size(); i++)
		    	   {
		    		   //변수(?s, ?p..)에 해당하는 결과 탐색 
		    		   RDFNode Resource = soln.get(variableList.get(i));	   
		    		   if(strFunc.checkIsUri(Resource.toString())){
		    			   tmpList.put(Resource.toString(), Resource.toString());
		    		
		    		   }
		    	   }
		      }
		      catch(ClassCastException e)
		      {
		    	  continue;
		      }
		     }
		 
		 Enumeration elist=tmpList.elements();
		 
		 while(elist.hasMoreElements())
		 {
			 String entity=(String) elist.nextElement();
			 entityList.add(new Entity(entity,this.sparqlEndPoint,entity,0,
					 entity));
		 }
		 
		 this.searcher.close();
		return  entityList;
	}
	
	public String makeSparql(String sparqlQuery)
	{
		return "prefix prop-ko:<http://ko.dbpedia.org/property/>"+"prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
				"prefix dc: <http://purl.org/dc/elements/1.1/>"+
				"prefix dcterms: <http://purl.org/dc/terms/>"+
				"prefix nlon: <http://lod.nl.go.kr/ontology/>"+
				"prefix jusop:<http://jusodata.kr/property/>"+"prefix jusoc:<http://jusodata.kr/class/>"+
				"prefix nlk: <http://lod.nl.go.kr/resource/>"+"PREFIX RDF: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
				"PREFIX foaf: <http://xmlns.com/foaf/0.1#>"+
				"PREFIX dct: <http://purl.org/dc/terms/> "
				+ 
				"PREFIX kdatap: <http://data.kdata.kr/property/>"+
		        "PREFIX hlod: <http://lod.koreanhistory.or.kr/>"+sparqlQuery;
	}
	
	public ArrayList<String> searchIntialURIList(String sparqlQuery)
	{
		System.out.println(sparqlQuery);
		
		ArrayList<String> variableList=this.strFunc.checkVariableDuplication(sparqlQuery);
		//변수 중복검사 (?s, ?p...)
		
		for(int i=0; i<variableList.size(); i++)
			System.out.println(variableList.get(i));
		
		return variableList;
	}

}
