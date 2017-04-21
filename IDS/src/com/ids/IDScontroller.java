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

	public static Que IDSQueue=new Que(); //심층검색을 위한 큐
	
	public LinkPolicyReader linkPolicyReader;
	
	private String surfaceUri;//표층검색 결과 Uri
	private String sparql;//표층검색 sparql;
	private int depth;//초기 설정 깊이
	private int similarity;//초기 설정 유사도
	

	public IDScontroller(LinkPolicyReader linkPolicyReader, String surfaceUri,String sparql,int depth,int similarity)
	{
		this.surfaceUri=surfaceUri;
		this.sparql=sparql;
		this.depth=depth;
		this.similarity= similarity;
		this.linkPolicyReader=linkPolicyReader;
	}
	
	public void searchingInLOD(IDSearcher IDS)
	{
	
		Model linkpolicy=null;
		HashSet<String> duplicationList=new HashSet<String>();
		Search surfaceSearcher=new Searcher();
		SurfaceSearching surfaceSearch=new SurfaceSearching(surfaceSearcher);
		surfaceSearch.surfaceSearch(sparql,surfaceUri,depth,similarity);
		//표층 검색 큐에 등록
		
		UriFilter uriFilter=new UriFilter();
		TripleAccumulator trAccumulator=new TripleAccumulator();
		int flag;
		
		while(!IDSQueue.isEmpty())
		{
			
			Qnode qNode=IDSQueue.deQueue();
			//큐에서 uri를 가져옴
			
			if((flag=uriFilter.checkNode(qNode))==0)
			{
				//uri의 http 접속 유무와 깊이 체크
				continue;
			}
				
			if(!checkDuplication(duplicationList,qNode))
			{
				//중복체크
				continue;
			}
			
			showURI(qNode);
			trAccumulator.writeRDF(qNode);
			
			linkpolicy=this.linkPolicyReader.getLinkPolicy(qNode);
			
			if(linkpolicy==null)
			{
				continue;
			}
			
			if(flag==1){
				IDS.setLinkPolicy(linkpolicy);
				IDS.setQnode(qNode);
				IDS.setSimilarity(similarity);
				IDS.search();
			}
		}
			
		trAccumulator.writeFile();
		
		
		
		
	}
	
	
	public boolean checkDuplication(HashSet<String> duplicationList,Qnode qNode)
	{
		
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
