package com.ids;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import com.controller.PESController;
import com.data.Entity;
import com.data.TripleInformation;
import com.reader.LinkPolicyReader;

public class ELS implements IDS{

	/**
	 * search Explicit link entities
	 * 
	 * depth: depth for search
	 */
	private int depth;
	private double percent;
	
	public ELS(double percent)
	{
		this.percent=percent;
	}
	
	public ELS()
	{
		
	}
	
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
	public void search(Entity source) {
		// TODO Auto-generated method stub
		
		String uri="";
		String highUri="";
		
		int p=source.getSameAsList().size();
		
			//TripleInformation.getSameAsList().size()
		for(int i=0; i<source.getExplicitSameAsList().size(); i++)
		{
			
			//Use sameAs info saved by TripleAccumulator
			uri=source.getExplicitSameAsList().get(i);
			
			if(source.getUri().equals(uri))
			//if target URI is same with source URI, Don't register in sameAsList
			{
				continue;
			}
			
			highUri=makeSparqlEndpoint(uri);
			//make SPARQL endpoint by uri that is sameAs uri
			Entity target=PESController.uriStorage.getEntity(uri); //get Entity by in uriStorage
			
			if(target==null)//first searched Entity
			{
				target=new Entity(uri,highUri,
						source.getSurfaceSearchUri(),1,source.getUri());
				
				source.getSameAsList().put(p, target);
				source.getDuplicationList().put(uri, uri);
				target.setExistInUriStorage(true);
				PESController.uriStorage.putEntity(target.getUri(), target);
				p++;
			}
			else// Entity already existed in uriStorage
			{
				if(source.getDuplicationList().get(uri)==null)
					//source don't have entity in sameAsList
				{
					source.getSameAsList().put(p,target);
					source.getDuplicationList().put(uri, uri);
					p++;
				}
				else
					continue;
			}
		}
		
	}
	

	@Override
	public LinkPolicyReader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReader(LinkPolicyReader reader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDepth(int depth) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getSimilarity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSimilarity(double similarity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void search(Entity source, double percent) {
		// TODO Auto-generated method stub
		
	}

	
}
