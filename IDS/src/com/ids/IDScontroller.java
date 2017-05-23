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
	IDScontroller get class that perform In-depth searching from IDSFactory
	
	**/
	
	public static Que IDSQueue=new Que(); //Queue for in-depth searching
	
	public LinkPolicyReader linkPolicyReader; //Reader for Link-Policy
	
	private String surfaceUri;//SPARQL EndpPoint of SourceLOD ex) http://ko.dbpedia.org/sparql
	private String sparql;//SPARQL Query;
	private int depth;//Depth for performing in-depth searhcing
	private double similarity;//Similarity for comparing object between sourceLOD and targetLOD
	private String filename;
	
	private HashSet<String> targetLODs; //targetLODs used for searching in-depth searching is used for OntologySearching
	
	public IDScontroller(String filename,LinkPolicyReader linkPolicyReader, String surfaceUri,String sparql,int depth,double similarity)
	{
		this.filename=filename;
		this.surfaceUri=surfaceUri;
		this.sparql=sparql;
		this.depth=depth;
		this.similarity=(1-similarity);
		this.linkPolicyReader=linkPolicyReader;
		this.targetLODs= new HashSet<String>();
	}
	
	public void searchingInLOD(IDSearcher IDS)
	{
	
		Model linkpolicy=null;
		HashSet<String> duplicationList=new HashSet<String>();
		Search surfaceSearcher=new Searcher();
		SurfaceSearching surfaceSearch=new SurfaceSearching(surfaceSearcher);
		surfaceSearch.surfaceSearch(sparql,surfaceUri,depth,similarity);
		
		//perform surfaceSearching whose LOD will become sourceLOD
		
		UriFilter uriFilter=new UriFilter();
		TripleAccumulator trAccumulator=new TripleAccumulator();
		int flag;
		
		while(!IDSQueue.isEmpty())
		{
			// perform in-depth searching by dequeuing queue node
			
			Qnode qNode=IDSQueue.deQueue();
			
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
			trAccumulator.writeRDF(qNode);
			//write Uri's info to rdf triple 
			
			
			linkpolicy=this.linkPolicyReader.getLinkPolicy(qNode);
			
			if(linkpolicy==null)
			{
				continue;
			}
			
			//Read Link-Policy to be relevent about Uri's sparql endpoint
			//if there are no Link-Policy matched, get next node in queue
			
			if(flag==1){
				//perform in-depth searching
				
				IDS.setLinkPolicy(linkpolicy);
				IDS.setQnode(qNode);
				IDS.setSimilarity(similarity);
				IDS.search();
			}
			
			targetLODs.add(qNode.getData().getSparqlEndpoint());
		}
			
		trAccumulator.writeFile(filename);
		trAccumulator.writeTargetLOD(targetLODs,surfaceUri);
		
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
