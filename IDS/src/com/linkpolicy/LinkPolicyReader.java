package com.linkpolicy;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import com.queue.Qnode;

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
		 
		 model.write(System.out, "RDF/XML-ABBREV"); //read Link-Policy
		
		 this.linkPolicy=model;
	}
	
	public Model getLinkPolicy(Qnode qNode)
	{
		
		Model model=null;
		try{

			model = ModelFactory.createDefaultModel();
			model.read(getLinkPolicyName(qNode.getData().getSparqlEndpoint())+".rdf");
			System.out.println("Link-Policy:"+getLinkPolicyName(qNode.getData().getSparqlEndpoint()));
		
			//make Link-Policy name using sparqlEndpoint by method getLinkPolicyName
			//*Link-Plicy name is unified check readmd
			
		}catch(org.apache.jena.riot.RiotNotFoundException e){
			System.out.println("There are no Link-Policy");
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
