package com.searcher;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.ws.http.HTTPException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Searcher implements Search{
	
	private QueryEngineHTTP qexec;
	private String query;
	private String sparqlEndPoint;
	public static ResultSet results;
	
	public Searcher()
	{
		
	}
	
	@Override
	public ResultSet executeQuery(String queryString,String sparqlEndpoint) {
		String apikey="YOUR API KEY";
		ResultSet results=null;
		
		try{
			Query query = QueryFactory.create(queryString) ;
		 qexec = QueryExecutionFactory.createServiceRequest(sparqlEndpoint, query);
		 qexec.addParam("apikey",apikey);
		 results = qexec.execSelect();
		 return results;
		 
		}
		catch(QueryException e)
		{
			//e.printStackTrace();
			//System.out.println("탐색 오류");
			return results;
		}
		catch( HTTPException e2)
		{
			//e2.printStackTrace();
			System.out.println("접속 오류");
			return null;
		}
	}
	
	public ResultSet executeQuery() {
		String apikey="YOUR API KEY";
		ResultSet results=null;
		
		try{
			Query query = QueryFactory.create(this.query) ;

		
		 qexec = QueryExecutionFactory.createServiceRequest(sparqlEndPoint, query);
		 qexec.addParam("apikey",apikey);
		 results = qexec.execSelect() ;
		 return results;
		 
		}
		catch(QueryException e)
		{
			//e.printStackTrace();
			//System.out.println("탐색 오류");
			return results;
		}
		catch( HTTPException e2)
		{
			//e2.printStackTrace();
			System.out.println("접속 오류");
			return null;
		}
	}
	
	@Override
	public void close()
	{
		if(qexec!=null){
		qexec.close();}
	}
	
	@Override
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public void setSparqlEndPoint(String sparqlEndPoint) {
		this.sparqlEndPoint=sparqlEndPoint;
	}
	
	
	
}
