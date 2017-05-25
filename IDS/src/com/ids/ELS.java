package com.ids;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jena.rdf.model.Model;

import com.data.TripleInformation;
import com.data.Uri;
import com.queue.Qnode;

public class ELS implements IDSearcher{

	private Qnode qNode;
	private String parentUri;
	
	public String makeSparqlEndpoint(String tmp)
	{
		//Make SPARQL endpoint by uri address
		
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		StringTokenizer st = new StringTokenizer(tmp,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(0)+"//"+tmp_str.get(1)+"/sparql";

	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		String uri="";
		String highUri="";
		int depth=qNode.getData().getDepth()-1;
		for(int i=0; i<TripleInformation.getSameAsList().size(); i++)
		{
			
			//Use sameAs info saved by TripleAccumulator
			uri=TripleInformation.getSameAsList().get(i);
			highUri=makeSparqlEndpoint(uri);
			//make SPARQL endpoint by uri that is sameAs uri
			
			IDScontroller.IDSQueue.enQueue(new Uri(uri,highUri,depth,qNode.getData().getsurfaceSearchUri(),2,qNode.getData().getParentUri()));
			//Enqueue uri with SPARQL endpoint created 
		}
		
}

	@Override
	public void setQnode(Qnode qNode) {
		// TODO Auto-generated method stub
		this.qNode=qNode;
	}

	@Override
	public void setLinkPolicy(Model linkPolicy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimilarity(double similarity) {
		// TODO Auto-generated method stub
		
	}
}
