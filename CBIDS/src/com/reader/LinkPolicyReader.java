package com.reader;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import com.data.Entity;

public class LinkPolicyReader {
	
	private Model linkPolicy;
	
	public LinkPolicyReader()
	{
		
	}
	
	public void readLinkPolicy(String linkPolicyName)
	{
		 // create an empty model
		 Model model = ModelFactory.createDefaultModel();

		 model.read(linkPolicyName+".rdf");
		 
		// model.write(System.out, "RDF/XML-ABBREV"); //read Link-Policy
		
		 this.linkPolicy=model;
	}
	
	public Model getLinkPolicy(Entity entity)
	{
		
		Model model=null;
		try{

			model = ModelFactory.createDefaultModel();
			model.read(getLinkPolicyName(entity.getSparqlEndPoint())+".rdf");
		
			//make Link-Policy name using sparqlEndpoint by method getLinkPolicyName
			//*Link-Plicy name is unified check readmd
			
		}catch(org.apache.jena.riot.RiotNotFoundException e){
			return null;
		}
		
		return model;
	}
	
	
	public String getLinkPolicyName (String tmp)
	{
		//Make Link-Policy file name using SPARQL Endpoint
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		StringTokenizer st = new StringTokenizer(tmp,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(1)+"_linkPolicy";
		//*Link-Plicy name is unified check 'readmd'

	}

}
