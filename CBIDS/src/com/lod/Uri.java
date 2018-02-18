package com.lod;

import java.util.Hashtable;

public class Uri {
	
	private String uri;
	private int depth;
	private Hashtable<String,String> incomingList;
	
	
	public Uri(String uri, int depth) {
		
		this.uri = uri;
		this.depth = depth;
		this.incomingList=new Hashtable<String,String>();
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void insertIncomingEdge(String uri)
	{
		this.incomingList.put(uri, uri);
	}
	public Hashtable<String, String> getIncomingList() {
		return incomingList;
	}
	public void setIncomingList(Hashtable<String, String> incomingList) {
		this.incomingList = incomingList;
	}
	
	
	

}
