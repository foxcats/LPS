package com.lod;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import com.calculator.Nomalization;
import com.data.Entity;
import com.searcher.Search;
import com.searcher.SparqlFactory;

public class LODConfidenceDetector {

	
	private ArrayList<Uri> list;
	private Hashtable<String,LOD> lodTable;
	private Hashtable<String,String> visitedUri;
	
	public LODConfidenceDetector()
	{
		this.list=new ArrayList<Uri>();
		this.lodTable=new Hashtable<String,LOD>();
		this.visitedUri=new Hashtable<String,String>();
	}
	
	public void detectLOD(Entity entity,int depth)
	{
		list.add(new Uri(entity.getUri(),depth));
		
		for(int i=0; i<list.size(); i++)
		{
			
			String sparqlEndPoint=makeSparqlEndPoint(list.get(i).getUri());
			String uri=list.get(i).getUri();
			int tmp_depth=list.get(i).getDepth();
			list.remove(i);
			
			if(visitedUri.get(uri)==null)
			{
				visitedUri.put(uri, uri);
			}
			else{
				continue;
			}
			
			
			if(depth!=0){
				searchLOD(uri,sparqlEndPoint,tmp_depth);
			}
		}
		
		show();
	}
	
	
	
	public void searchLOD(String uri,String sparqlEndPoint,int depth)
	{
		Search search=new SparqlFactory().getSearcher();
		String sparql="select ?o { <"+uri+"> <http://www.w3.org/2002/07/owl#sameAs> ?o}";
		ResultSet results=search.executeQuery(sparql,sparqlEndPoint);
		
		if(results==null)
			return;
		
		while(results.hasNext())
		{
			QuerySolution soln = results.nextSolution() ;
		    RDFNode object= soln.get("?o");
		    list.add(new Uri(object.toString(),(depth-1)));
		    makeLOD(object.toString());
		}
	}
	
	public String makeSparqlEndPoint(String uri)
	{
		ArrayList<String> tmp_str=new ArrayList<String>();
		
		StringTokenizer st = new StringTokenizer(uri,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(0)+"//"+tmp_str.get(1)+"/sparql";
	}
	
	public void makeLOD(String uri)
	{
		String sparqlEndPoint=makeSparqlEndPoint(uri);
		LOD lod=this.lodTable.get(sparqlEndPoint);
		if(lod==null)
		{
			lod=new LOD(sparqlEndPoint);
			lod.setLodN(0);
			
			this.lodTable.put(sparqlEndPoint, lod);
		}
		else{
			int num=lod.getLodN();			
			lod.setLodN(num+1);
			
		}
	}
	
	public void show()
	{
		Enumeration elist=this.lodTable.elements();
		Nomalization nm=new Nomalization();
		int index=0;
		nm.standardization2(this.lodTable);
		while(elist.hasMoreElements())
		{
			LOD lod=(LOD)elist.nextElement();
			System.out.println(lod.getLOD()+" "+lod.getLodConfidence());
			
		}
		
	}

	public ArrayList<Uri> getList() {
		return list;
	}

	public void setList(ArrayList<Uri> list) {
		this.list = list;
	}

	public Hashtable<String, LOD> getLodTable() {
		return lodTable;
	}

	public void setLodTable(Hashtable<String, LOD> lodTable) {
		this.lodTable = lodTable;
	}

	public Hashtable<String, String> getVisitedUri() {
		return visitedUri;
	}

	public void setVisitedUri(Hashtable<String, String> visitedUri) {
		this.visitedUri = visitedUri;
	}
	
	
	
}
