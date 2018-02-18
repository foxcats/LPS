package com.searcher;

import org.apache.jena.query.ResultSet;

public interface Search {
	public ResultSet executeQuery(String queryString,String sparqlEndpoint);
	public ResultSet executeQuery();
	public void setQuery(String query);
	public void setSparqlEndPoint(String sparqlEndpoint);
	public void close();

}
