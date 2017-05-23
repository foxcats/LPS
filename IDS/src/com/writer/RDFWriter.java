package com.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.InvalidPropertyURIException;

public class RDFWriter implements writer {
	
	/**
	 * writer that write RDF data searched in targetLOD by in-depth searching
	 */
	
	private String subject;
	private Model model;
	private Resource r;
	private ArrayList<Model> modelList;
	private HashMap<String,Resource> surfaceUriList=new HashMap<String,Resource>();
	
	RDFWriter()
	{
		this.model=ModelFactory.createDefaultModel();
	
	}
	
	@Override
	public void makeSubject(String subject)
	{
		
		this.subject=subject;
		surfaceUriList.put(subject,this.model.createResource(subject));
	}
	
	
	@Override
	public void makeRDF(String surfaceSearchUri,String predicate, String object)
	{
		Property p=model.createProperty(predicate);
		
		if(p.isProperty())
		{
			surfaceUriList.get(surfaceSearchUri).addProperty(p, object);
			//System.out.println("속성등록");
		}
		else
		{
			System.out.println("등록불가");
		}
		
	}
	
	@Override
	public void writeFile(String filename)
	{
		FileWriter out = null;
		try {
			
			System.out.println("write RDF");
			out = new FileWriter(filename+".nt");

			model.write(out,"N-TRIPLES");
			
			out.close();
			
		} catch (IOException e) {
			
		}
		catch(InvalidPropertyURIException e)
		{
			System.out.println("writing error");
		}
	}
	
	@Override
	public Model getModel()
	{
		return this.model;
	}

}
