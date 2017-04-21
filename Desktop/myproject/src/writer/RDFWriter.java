package writer;

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
		//System.out.println("주어 생성");
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
	public void writeFile()
	{
		FileWriter out = null;
		try {
			
			System.out.println("RDF 기록");
			out = new FileWriter("t아이언맨_4_0.nt");

			model.write(out,"N-TRIPLES");
			
			out.close();
			
		} catch (IOException e) {
			
		}
		catch(InvalidPropertyURIException e)
		{
			System.out.println("에러");
		}
	}
	
	@Override
	public Model getModel()
	{
		return this.model;
	}

}
