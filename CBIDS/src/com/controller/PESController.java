package com.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.data.EC;
import com.data.Entity;
import com.filter.UriFilter;
import com.ids.IDS;
import com.lod.LOD;
import com.searcher.Search;
import com.searcher.Searcher;
import com.storage.LODStorage;
import com.storage.URIStorage;
import com.writer.RDFWriter;
import com.writer.TripleAccumulator;
import com.writer.writer;

public class PESController {
	/**
	 * PES(Policy-based Expanded Search)
	 * perform IDS(LPS, ELS, CLS) 
	 * LPS: Link Policy-based Search
	 * ELS: Explicit link-based Search
	 * CLS: LPS+ELS
	 */

	public static URIStorage uriStorage=new URIStorage();
	public static LODStorage lodStorage=new LODStorage();
	public static Hashtable<String,String> preventLOD=new Hashtable<String,String>();
	public Hashtable<String,Entity> validURI=new Hashtable<String,Entity>();
	
	public Hashtable<Entity, Entity> searchedList=new Hashtable<Entity, Entity>();	
	ArrayList<Entity> uriList;
	
	public static int agreement=0;
	public static int allAgreement=0;
	public static int count=0;
	
	
	public int depthCount=0;
	
	public void search(Entity sfsEntity,int depth,IDS ids,Hashtable<String,LOD> lodList,String fileName)//Start 'Policy-based Expanded Search'
	{
		Search searcher=new Searcher();
		UriFilter filter=new UriFilter();
		writer rdfWriter=new RDFWriter();
		TripleAccumulator tripleAccumulator=new TripleAccumulator(searcher,rdfWriter,filter,sfsEntity.getUri());
		
		
		this.lodStorage.setLodList(lodList);
		this.uriList=new ArrayList<Entity>();
		uriList.add(sfsEntity);//insert sfsEntity in Uri-List
		
		int index=0;
		ids.setDepth(depth);//set depth for In-depth search(IDS)
		
		
		uriStorage.putEntity(sfsEntity.getUri(), sfsEntity);//insert sfsEntity in uriStorage, we will get entity in uriStorage entity have already existed in
		
		
		sfsEntity.setEntityConfidence(1);//set EC to 1
		sfsEntity.setDepth(depth);
		int num=0;
		
	
		while(true)
		{
			
			if(uriList.isEmpty())//check Uri-List is empty
			{
				break;
				//end PES
			}
			Entity source=uriList.get(index);
			//get entity to Uri-List 
			System.out.println(source.getUri());
			
			if(source.getDepth()==0)
			{
				tripleAccumulator.writeRDFTest(source);
				sourceClear(source);
				uriList.remove(index);
				index--;
				continue;
				
			}
			
			
			if(!tripleAccumulator.writeRDFTest(source))//accumulate URI Triple
			{
				
				sourceClear(source);
				uriList.remove(index);
				index--;
				continue;
			}
			
			
			this.validURI.put(source.getUri(), source);
			
			if(source.getSameAsList().isEmpty())
			{
				ids.search(source);
			}
			
			if(source.getSameAsList().size()==source.getIndex())
			{
					sourceClear(source);
					uriList.remove(index);
					index--;
					continue;
			}
				
				
			Entity target=source.getSameAsList().get(source.getIndex());
			source.setIndex(source.getIndex()+1);
			index=performCRC(source,target,index,rdfWriter);
			
		
			num++;
		}
		 this.agreement=0;
		 this.allAgreement=0;
		 System.out.println(num);
		 clearValidity();
		
		this.lodStorage.getLodList().clear();
		this.uriStorage.getUriStorage().clear();
		
		rdfWriter.writeFile(fileName);
		
	}
	
	public void searchTest(Entity sfsEntity,IDS ids,Hashtable<String,LOD> lodList)//Start 'Policy-based Expanded Search'
	{
		Search searcher=new Searcher();
		UriFilter filter=new UriFilter();
		writer rdfWriter=new RDFWriter();
		TripleAccumulator tripleAccumulator=new TripleAccumulator(searcher,rdfWriter,filter,sfsEntity.getUri());
		
		this.lodStorage.setLodList(lodList);
		this.uriList=new ArrayList<Entity>();
		uriList.add(sfsEntity);//insert sfsEntity in Uri-List
		
		int index=0;
		
		
		
		uriStorage.putEntity(sfsEntity.getUri(), sfsEntity);//insert sfsEntity in uriStorage, we will get entity in uriStorage entity have already existed in
		
		
		sfsEntity.setEntityConfidence(1);//set EC to 1
		int num=0;
		
		while(true)
		{
			
			if(uriList.isEmpty())//check Uri-List is empty
			{
				//currentEntityList.clear();
				break;
				//end PES
			}
			
			Entity source=uriList.get(index);
			//get entity to Uri-List 
			/*
			if(!filter.checkNode(source))//check uri validity
			{
				sourceClear(source);
				uriList.remove(index);
				index--;
				
				if(this.depthCount!=0){
					this.depthCount--;
				}
				
				continue;
			}*/
			
			System.out.println("source: "+ source.getUri());
			
			if(!tripleAccumulator.writeRDF(source))//accumulate URI Triple
			{
				
				sourceClear(source);
				uriList.remove(index);
				index--;
				
				if(this.depthCount!=0){
					this.depthCount--;
				}
				
				continue;
			}
			
			
			this.validURI.put(source.getUri(), source);
			
			if(source.getSameAsList().isEmpty())
			{
				ids.search(source);
			}
			
			if(source.getSameAsList().size()==source.getIndex())
			{
					sourceClear(source);
					show(uriList,rdfWriter);
					uriList.remove(index);
					index--;
					
					if(this.depthCount!=0){
						this.depthCount--;
					}
					continue;
			}
				
				
			Entity target=source.getSameAsList().get(source.getIndex());
			System.out.println("   target: "+target.getUri()+" "+target.getSameAsList().size()+" "+target.getIndex());
			
			source.setIndex(source.getIndex()+1);
			index=performCRC2(source,target,index,rdfWriter);
			
		
			num++;
		}
		
		
		showEC();
		//showSameAsCount();
		

		 this.lodStorage.getLodList().clear();
		 this.uriStorage.getUriStorage().clear();
		 this.agreement=0;
		 this.allAgreement=0;
		 System.out.println(num);
		 rdfWriter.showNum();
	}
	
	public void clearValidity(){
		Enumeration test=this.uriStorage.getUriStorage().elements();
		
		while(test.hasMoreElements()){
			Entity tmp=(Entity)test.nextElement();
			tmp.setValidity(-1);
		}
	}
	
	public int performCRC(Entity source,Entity target,int index,writer rdfWriter)
	{
		CRCController crcController=new CRCController();
		
		if(!crcController.confidenceCleansing(source, target))
		{
			return index;
			
		}else{
			target.setDepth(source.getDepth()-1);
			uriList.add(target);
			source.setVisitFlag(1);
			count++;
			index++;
		}
		return index;
	}
	
	
	public int performCRC2(Entity source,Entity target,int index,writer rdfWriter)
	{
		CRCController crcController=new CRCController();
		
		if(!crcController.confidenceCleansing2(source, target))
		{
			return index;
			
		}else{
			uriList.add(target);
			source.setVisitFlag(1);
			count++;
			index++;
			this.depthCount++;
		}
		return index;
	}
	
	
	

	public boolean startSearchIDS(Entity source,IDS ids)
	{
		
		
		return true;
	}
	
	public void sourceClear(Entity source)
	{
		source.setDepth(-1);
		source.setIndex(0);
		source.setVisitFlag(0);
	}
	
	public void insertData(Entity source)
	{
		if(source.getVisitFlag()!=1)
		{
			//this.currentEntityList.add(source);
		}
		
	}
	
	public void show(ArrayList<Entity> uriList,writer rdfWriter)
	{
		String currentList="";
		int agreement=0;
		System.out.println("-----------------------------------");
		System.out.println("최종깊이:"+(this.depthCount+1)+" "+this.uriStorage.getUriStorage().size()+" "+this.lodStorage.getLodList().size()+" "+this.preventLOD.size()+
				" "+this.validURI.size()+" "+this.searchedList.size()+" "+this.uriList.size());
		for(int i=0; i<uriList.size(); i++)
		{
			if(i<(uriList.size()-1))
			{
				agreement+=i;
			}
			
			if(uriList.get(i).getVisitFlag()==0)
			{
				System.out.println(uriList.get(i).getUri()+"의 방문플레그 문제");
			}
			
			System.out.println(uriList.get(i).getSparqlEndPoint()+" "+PESController.lodStorage.getLC(uriList.get(i)).getLodConfidence());
			currentList+=(uriList.get(i).getUri()+"$"+uriList.get(i).getEntityConfidence()+" ");
		}
		
		System.out.println(currentList);
		//rdfWriter.showNum();
		this.allAgreement+=agreement;
		System.out.println("-----------------------------------");
	}
	
	public void show(ArrayList<Entity> uriList,Entity target,writer rdfWriter)
	{
		String currentList="";
		System.out.println("-----------------------------------");
		System.out.println("동조 / 최종깊이:"+(this.depthCount+1)+" "+this.uriStorage.getUriStorage().size()+" "+this.lodStorage.getLodList().size()+" "+this.preventLOD.size()+
				" "+this.validURI.size()+" "+this.searchedList.size()+" "+this.uriList.size());
		for(int i=0; i<uriList.size(); i++)
		{
			
			System.out.println(uriList.get(i).getSparqlEndPoint()+" "+PESController.lodStorage.getLC(uriList.get(i)).getLodConfidence());
			currentList+=(uriList.get(i).getUri()+"$"+uriList.get(i).getEntityConfidence()+"$"+uriList.get(i).getDepth()+" ");
			
		}
		currentList+=(target.getUri()+"$"+target.getEntityConfidence());
		
		System.out.println(currentList);
		//rdfWriter.showNum();
		System.out.println("-----------------------------------");
	}
	
	
	public void showEC()
	{
		Enumeration e=this.uriStorage.getUriStorage().elements();
		
		while(e.hasMoreElements())
		{
			Entity entity=(Entity)e.nextElement();
			System.out.println(entity.getUri());
			for(int i=0; i<entity.getDc().getConfidenceList().size(); i++)
			{
				EC ec=entity.getDc().getConfidenceList().get(i);
				System.out.print(ec.getEc()+"/"+ec.getCount()+"  ");
			}
			System.out.println("");
		}
		
		System.out.println("참여 개체:"+this.validURI.size());
		Enumeration e2=validURI.elements();
		int num=0;
		while(e2.hasMoreElements())
		{
			Entity entity=(Entity)e2.nextElement();
			System.out.println(" "+entity.getUri()+" "+entity.getSameAsList().size());
			num+=entity.getSameAsList().size();
		}
		System.out.println("발생한 동조 수: "+PESController.agreement);
		System.out.println("총 sameAs 수: "+num);

	}
	
	public void showSameAsCount()
	{
		Enumeration e=this.uriStorage.getUriStorage().elements();
		int count=1;
		int count2=1;
		while(e.hasMoreElements())
		{
			Entity entity=(Entity)e.nextElement();
			
			for(int i=0; i<entity.getExplicitSameAsList().size(); i++)
			{
				if(this.preventLOD.get(entity.getExplicitSameAsList().get(i))==null)
				{
					count++;
				}
				count2++;
			}
		}
		
		System.out.println("총 owl:sameAs 연결:"+count);
		System.out.println("총 owl:sameAs 연결2:"+count2);
	}
}
