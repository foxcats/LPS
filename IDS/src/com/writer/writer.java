package com.writer;

import org.apache.jena.rdf.model.Model;

public interface writer {
	

	public void makeSubject(String subject);
	public void makeRDF(String surfaceSearchUri,String predicate, String object);
	public void writeFile(String filename);
	public Model getModel();
}
