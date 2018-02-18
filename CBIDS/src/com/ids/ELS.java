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
		
		
		if(percent==0){
			
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
		else{
			
			Random random=new Random();
			int num=(int) (source.getExplicitSameAsList().size()*percent);
			
			int sameAsNum=source.getExplicitSameAsList().size();
			ArrayList<String> exlicitList=new ArrayList<String>();
			for(int i=0; i<num; i++)
			{
				
				int index=random.nextInt(source.getExplicitSameAsList().size()-1);
				
				if(source.getExplicitSameAsList().get(index) == null)
				{
					i++;
					i--;
					continue;
				}
				
				//Use sameAs info saved by TripleAccumulator
				uri=source.getExplicitSameAsList().get(index);
				source.getExplicitSameAsList().remove(index);
				
	
			}
			
			for(int i=0; i<source.getExplicitSameAsList().size(); i++)
			{
	
				if(source.getExplicitSameAsList().get(i)==null)
				{
					continue;
				}
				
				uri=source.getExplicitSameAsList().get(i);
				
				if(source.getUri().equals(uri))
					//if target URI is same with source URI, Don't register in sameAsList
					{
						System.out.println("주어와일치");
						continue;
					}
				
				highUri=makeSparqlEndpoint(uri);
				//make SPARQL endpoint by uri that is sameAs uri
				Entity target=PESController.uriStorage.getEntity(uri);
				
				if(target==null)
				{
					target=new Entity(uri,highUri,
							source.getSurfaceSearchUri(),1,source.getUri());
					
					source.getSameAsList().put(p, target);
					source.getDuplicationList().put(uri, uri);
					target.setExistInUriStorage(true);
					PESController.uriStorage.putEntity(target.getUri(), target);
					p++;
				}
				else
				{
					if(source.getDuplicationList().get(uri)==null)
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
