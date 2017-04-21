package com.data;

import java.io.Serializable;

public class Uri implements Serializable{
	
	private String uri;
	private String sparqlEndpoint;
	private int depth;
	private int flag;
	private String surfaceSearchUri;
	private String parentUri;



	public String getParentUri() {
		return parentUri;
	}

	public void setParentUri(String parentUri) {
		this.parentUri = parentUri;
	}

	public Uri(){
		super();
	}

	public Uri(String uri,String highUri,int depth,String surfaceSearchUri,int flag, String parentUri)
	{
		super();
		this.uri=uri;
		this.sparqlEndpoint=highUri;
		this.depth=depth;
		this.surfaceSearchUri=surfaceSearchUri;
		this.flag=flag;
		this.parentUri=parentUri;
	}
	

	public int getFlag()
	{
		return flag;
	}
	public void setUri(String sourceUri)
	{
		uri=sourceUri;
		
	}
	
	public void setsSparqlEndpoint(String sparqlEndpoint)
	{
		sparqlEndpoint=sparqlEndpoint;
		
	}
	
	public String getUri()
	{
		if(uri==null) return "";
		else
			return uri;
	}
	
	public String getSparqlEndpoint()
	{
		if(sparqlEndpoint==null) return "";
		else
			return sparqlEndpoint;
	}
	
	public String getsurfaceSearchUri()
	{
		if(surfaceSearchUri==null) return "";
		else
			return surfaceSearchUri;
	}

	public int getDepth()
	{
		return depth;
	}
	
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append("uri=").append(uri).append("highUri=").append(sparqlEndpoint).append("depth=").append(depth);
		return sb.toString();
	}
}
