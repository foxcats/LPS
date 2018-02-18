package com.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import com.data.Entity;
import com.filter.UriFilter;
import com.ids.IDS;
import com.lod.LODConfidenceDetector;
import com.reader.LinkPolicyReader;

public class IDSController {

	/**
	IDScontroller get class that perform In-depth searching from IDSFactory
	 depth: Depth for performing in-depth searhcing
	 sfsList: surface searching results
	 ec: entity identical confidence
	 filename: file title
	 linkPolicyReader: reader has read link-policy
	**/
	
	
	public static Hashtable<String,Entity> entityList=new Hashtable<String,Entity>();	
	public LinkPolicyReader linkPolicyReader; //Reader for Link-Policy

	private ArrayList<Entity> sfsList;
	
	private int depth;//Depth for performing in-depth searhcing
	private String filename;

	public double ec;
	
	
	public IDS ids;
	
	public IDSController(String filename, ArrayList<Entity> sfsList,int depth,double ec,IDS ids)
	{
		this.filename=filename;
		this.depth=depth;
		this.sfsList=sfsList;
		this.ids=ids;
		this.ec=ec;
	}
	
	public IDSController(String filename, ArrayList<Entity> sfsList,double ec,IDS ids)
	{
		this.filename=filename;
		this.sfsList=sfsList;
		this.ids=ids;
		this.ec=ec;
		this.depth=-1;
	}
	
	
	public void search()
	{
		PESController pes=new PESController();
		
	
		UriFilter filter=new UriFilter();
		LODConfidenceDetector loddetector=new LODConfidenceDetector();
		int index=0;
		
		for(int i=0; i<this.sfsList.size(); i++)//start ids use sfs results
		{
			Entity sfsEntity=this.sfsList.get(i);
			
			if(!sfsEntity.getUri().equals("http://ko.dbpedia.org/resource/아이언맨_(영화)"))
			{
				
				continue;
			}
			
			//loddetector.detectLOD(sfsEntity, 2);//calculate LOD Confidence(LC)
			
		
			pes.search(sfsEntity,this.depth,this.ids,loddetector.getLodTable(),this.filename);
			
			System.out.println("----------------------------------------------------------------------");
		}
	}
	
}
