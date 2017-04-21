package Searcher;

import org.apache.jena.query.ResultSet;

public interface Search {
	public ResultSet executeQuery(String queryString,String sparqlEndpoint);
	public void close();

}
