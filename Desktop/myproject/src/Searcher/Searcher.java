package Searcher;

import javax.xml.ws.http.HTTPException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Searcher implements Search{
	
	private QueryEngineHTTP qexec;
	
	@Override
	public ResultSet executeQuery(String queryString,String sparqlEndpoint) {
		String apikey="YOUR API KEY";
		ResultSet results=null;
		
		try{
			Query query = QueryFactory.create(queryString) ;

		
		 qexec = QueryExecutionFactory.createServiceRequest(sparqlEndpoint, query);
		 qexec.addParam("apikey",apikey);
		 results = qexec.execSelect() ;
		 return results;
		 
		}
		catch(QueryException e)
		{
			//e.printStackTrace();
			System.out.println("탐색 오류");
			return results;
		}
		catch( HTTPException e2)
		{
			//e2.printStackTrace();
			System.out.println("접속 오류");
			return null;
		}
	}
	
	public void close()
	{
		qexec.close();
	}
	
}
