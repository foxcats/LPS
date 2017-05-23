package com.ids;

import java.util.HashSet;

import org.apache.jena.rdf.model.Model;

import com.filter.UriFilter;
import com.linkpolicy.LinkPolicyReader;
import com.queue.Qnode;
import com.queue.Que;
import com.searcher.Search;
import com.searcher.Searcher;
import com.sfs.SurfaceSearching;
import com.writer.TripleAccumulator;

public class IDScontroller {

	/**
	IDScontroller get class that perform In-depth searching from IDSFactory using method 'searchingInLOD'
	**/
	
	public static Que IDSQueue=new Que(); //Queue for in-depth searching
	
	public LinkPolicyReader linkPolicyReader;//Reader for Link-Policy
	
	private String surfaceUri;//SPARQL EndpPoint of SourceLOD ex) http://ko.dbpedia.org/sparql
	private String sparql;//SPARQL Query
	private int depth;//Depth to perform in-depth searhcing
	private int similarity;//Similarity to compare object between sourceLOD and targetLOD
	

	public IDScontroller(LinkPolicyReader linkPolicyReader, String surfaceUri,String sparql,int depth,int similarity)
	{
		this.surfaceUri=surfaceUri;
		this.sparql=sparql;
		this.depth=depth;
		this.similarity= (1-similarity);
		this.linkPolicyReader=linkPolicyReader;
	}
	
	public void searchingInLOD(IDSearcher IDS)
	{
	
		Model linkpolicy=null;
		HashSet<String> duplicationList=new HashSet<String>();
		Search surfaceSearcher=new Searcher();//make a searcher to execute sparql query
		SurfaceSearching surfaceSearch=new SurfaceSearching(surfaceSearcher);// start surface searching
		surfaceSearch.surfaceSearch(sparql,surfaceUri,depth,similarity);//insert results of surface searching in queue
	
		
		UriFilter uriFilter=new UriFilter();//make UriFilter
		TripleAccumulator trAccumulator=new TripleAccumulator();//make TripleAccumulator
		int flag;
		
		while(!IDSQueue.isEmpty())
		{
			
			Qnode qNode=IDSQueue.deQueue();
			//get Node from queue
			
			if((flag=uriFilter.checkNode(qNode))==0)
			{
				//Check URI's validity 
				continue;
			}
				
			if(!checkDuplication(duplicationList,qNode))
			{
				//Check duplication in queue 
				continue;
			}
			
			showURI(qNode);
			trAccumulator.writeRDF(qNode);//write Uri's info by rdf triple 
			
			linkpolicy=this.linkPolicyReader.getLinkPolicy(qNode); //Read Link-Policy to be relevent about Uri's sparql endpoint
			
			if(linkpolicy==null)//if there are no Link-Policy matched, get next node in queue
			{
				continue;
			}
			
			if(flag==1){
				IDS.setLinkPolicy(linkpolicy); //set Link-Policy you read above
				IDS.setQnode(qNode);//set qNode you read above
				IDS.setSimilarity(similarity);//set similarity 
				IDS.search();//start IDS(LPS, EPS or CLS
			}
		}
			
		trAccumulator.writeFile();//output result
		
		
		
		
	}
	
	
	public boolean checkDuplication(HashSet<String> duplicationList,Qnode qNode)
	{
		//Duplication check using HashSet
		int duplicationListNum=duplicationList.size();
		
		duplicationList.add(qNode.getData().getUri()+qNode.getData().getsurfaceSearchUri());
	
		int duplicationListNum2=duplicationList.size();
	
		if(duplicationListNum==duplicationListNum2)
		{
			return false;
		}
		
		return true;
	}
	
	public void showURI(Qnode qNode)
	{
		if(qNode.getData().getFlag()==2){
			System.out.println("uri: "+qNode.getData().getUri()+"-------FROM "+qNode.getData().getParentUri()+" Result of ELS");
		}
		else if(qNode.getData().getFlag()==1){
			System.out.println("uri: "+qNode.getData().getUri()+"-------FROM "+qNode.getData().getParentUri()+" Result of LPS");
		}
		else if(qNode.getData().getFlag()==0)
		{
			System.out.println("uri: "+qNode.getData().getUri()+"-------FROM "+qNode.getData().getParentUri()+" Result of SurfaceSearching");
		}
	
	}
}
