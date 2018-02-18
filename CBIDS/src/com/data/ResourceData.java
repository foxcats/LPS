package com.data;

import org.apache.jena.rdf.model.Resource;

public class ResourceData {
	
	private String surfaceUri;
	private Resource resource;
	private int size;
	
	public ResourceData(String surfaceUri, Resource resource){
		this.surfaceUri=surfaceUri;
		this.resource=resource;
		this.size=0;
	}
	
	public String getSurfaceUri() {
		return surfaceUri;
	}
	public void setSurfaceUri(String surfaceUri) {
		this.surfaceUri = surfaceUri;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public void increaseSize(){
		size++;
	}

	
}
