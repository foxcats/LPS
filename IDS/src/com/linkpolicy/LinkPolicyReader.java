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

		 model.read(linkPolicyName+".rdf");/*
		 // use the FileManager to find the input file
		 FileInputStream in = (FileInputStream) FileManager.get().open( linkPolicyName+".rdf" );
		if (in == null) {
		    throw new IllegalArgumentException(
		                                 "File: " + linkPolicyName + " not found");
		}

		// read the RDF/XML file
		model.read(in,null);
		model.read*/
		model.write(System.out, "RDF/XML-ABBREV");
		
		this.linkPolicy=model;
	}
	
	public Model getLinkPolicy(Qnode qNode)
	{
		
		Model model=null;
		try{

			model = ModelFactory.createDefaultModel();
			model.read(getLinkPolicyName(qNode.getData().getSparqlEndpoint())+".rdf");
			System.out.println("연결정책:"+getLinkPolicyName(qNode.getData().getSparqlEndpoint()));
		
		}catch(org.apache.jena.riot.RiotNotFoundException e){
			System.out.println("연결정책 없음");
			return null;
		}
		
		return model;
	}
	
	
	public String getLinkPolicyName (String tmp)
	{
		//sparqlEndPoint 생성
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		StringTokenizer st = new StringTokenizer(tmp,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(1)+"_linkPolicy";

	}

}
