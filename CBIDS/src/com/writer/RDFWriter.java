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

import com.data.ResourceData;

public class RDFWriter implements writer {
	
	/**
	 * writer that write RDF data searched in targetLOD by in-depth searching
	 */
	
	private String subject;
	private Model model;
	private HashMap<String,ResourceData> surfaceUriList=new HashMap<String,ResourceData>();
	
	public RDFWriter()
	{
		this.model=ModelFactory.createDefaultModel();
	
	}
	
	@Override
	public void makeSubject(String subject)
	{
		
		this.subject=subject;
		Resource subjectResource=this.model.createResource(subject);
		surfaceUriList.put(subject,new ResourceData(subject, subjectResource));
	}
	
	
	@Override
	public void makeRDF(String surfaceSearchUri,String predicate, String object)
	{
		Property p=model.createProperty(predicate);
		
		if(p.isProperty())
		{
			ResourceData subject=surfaceUriList.get(surfaceSearchUri);
			subject.getResource().addProperty(p, object);
			subject.increaseSize();
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
	
	public void showNum(){
		ResourceData tmp=this.surfaceUriList.get(this.subject);
		System.out.println("최종 트리플 수:"+tmp.getSize());
	}

}
