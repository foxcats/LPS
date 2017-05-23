package com.sfs;

import java.util.ArrayList;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import com.data.Uri;
import com.ids.IDScontroller;
import com.searcher.Search;
import com.stringhandler.StringFunction;

public class SurfaceSearching {
	
   private ArrayList<String> variableList;
   private StringFunction strFunc;
   private Search surfaceSearcher;
   
	public SurfaceSearching(Search surfaceSearcher)
	{
		this.surfaceSearcher=surfaceSearcher;
		this.variableList=new ArrayList<String>();
		this.strFunc=new StringFunction();
	}
	
	public void surfaceSearch(String sparqlQuery, String sparqlEndpoint,int depth,double similarity)
	{
		ArrayList<Uri> surfaceUriList=search(sparqlQuery,sparqlEndpoint,depth,similarity);
		
		
		
		for(int i=0; i<surfaceUriList.size(); i++)	
		{
			//System.out.println(surfaceUriList.get(i).getUri());
			IDScontroller.IDSQueue.enQueue(surfaceUriList.get(i));
		}//표층 검색  큐에 등록 등록
		
		
	}
	
	
	public ArrayList<Uri> search(String sparqlQuery, String sparqlEndpoint,int depth,double similarity){
		
		sparqlQuery=makeSparql(sparqlQuery);
		
		searchIntialURIList(sparqlQuery);
		ResultSet results=surfaceSearcher.executeQuery(sparqlQuery, sparqlEndpoint);
		
		
		ArrayList<Uri> uriList=new ArrayList<Uri>();
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		       try{
		    	 
		    	   for(int i=0; i<variableList.size(); i++)
		    	   {
		    		   //변수(?s, ?p..)에 해당하는 결과 탐색 
		    		   RDFNode Resource = soln.get(variableList.get(i));	    		   
		 
		    		   if(strFunc.checkIsUri(Resource.toString())){
		    			  uriList.add(new Uri(Resource.toString(),sparqlEndpoint,depth,Resource.toString(),0,
		    					  Resource.toString()));
		    		
		    		   }
		    	   }
		      }
		      catch(ClassCastException e)
		      {
		    	  continue;
		      }
		     }
		 
		 surfaceSearcher.close();
		 
		 return uriList;
		
		
	}
	
	public void searchIntialURIList(String sparqlQuery)
	{
		
		System.out.println(sparqlQuery);
		
		variableList=strFunc.checkVariableDuplication(sparqlQuery);
		//변수 중복검사 (?s, ?p...)
		
		for(int i=0; i<variableList.size(); i++)
			System.out.println(variableList.get(i));
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
}
