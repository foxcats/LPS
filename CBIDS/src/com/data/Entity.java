package com.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.lod.LOD;
import com.storage.DepthCollector;
public class Entity {

	/**
	 * Entity class searched in LOD
	 * 
	 * sameAsList: save searched Entiies in LOD by LPS and ELS
	 * duplicationList: check already inserted Entity
	 * dc: collect ec(entity identical confidence)  at all depth
	 * explicitSameAsList: save explicit-links
	 * index: index for sameList
	 * visitFlag: show state of visiting 0: not visit 1: visited
	 * entityConfidence: Entity identical Confidence
	 * uri: entity's uri
	 * sparqlEndPoint: sparqlEndPoint
	 * depth: depth for ids
	 * searchflag: els-> 0 lps-> 1 sfs-> -1
	 * edgeCount: count incoming link
	 * surfaceSearchUri: save surface search URI
	 * parentUri: save Entity's parent entity's URI
	 */
	
	private Hashtable<Integer, Entity> sameAsList;
	private Hashtable<String,String> duplicationList;
	private DepthCollector dc;
	private ArrayList<String> explicitSameAsList;
	
	
	private int index;
	private int visitFlag;
	private boolean existInUriStorage;
	
	private double entityConfidence;
	private String uri;
	private String sparqlEndPoint;
	
	
	private int depth;
	private int searchflag;
	private int edgeCount;
	private String surfaceSearchUri;//표층검색 결과에 해당하는 최초 개체
	private String parentUri;//앞 깊이의 부모 개체
	
	private int validity;
	
	
	private ArrayList<PODataList> tripleInfo;//개체의 트리플 정보
	
	public Entity(String uri,String sparqlEndPoint,int depth,String surfaceSearchUri,int flag, String parentUri)
	{
		this.sameAsList=new Hashtable<Integer, Entity>();
		this.duplicationList=new Hashtable<String,String>();
		this.index=0;
		this.visitFlag=0;
		this.entityConfidence=0;
		this.uri=uri;
		this.sparqlEndPoint=sparqlEndPoint;
		
		this.depth=depth;
		this.surfaceSearchUri=surfaceSearchUri;
		this.searchflag=flag;
		this.parentUri=parentUri;
		
		this.tripleInfo=new ArrayList<PODataList>();
		this.dc=new DepthCollector();
		this.edgeCount=0;
		
		this.explicitSameAsList=new ArrayList<String>();
		this.existInUriStorage=false;
		this.validity=-1;
	}


	public Entity(String uri,String sparqlEndPoint,String surfaceSearchUri,int flag, String parentUri)
	{
		this.sameAsList=new Hashtable<Integer, Entity>();
		this.duplicationList=new Hashtable<String,String>();
		this.index=0;
		this.visitFlag=0;
		this.entityConfidence=0;
		this.uri=uri;
		this.sparqlEndPoint=sparqlEndPoint;
		this.surfaceSearchUri=surfaceSearchUri;
		this.searchflag=flag;
		this.parentUri=parentUri;
		this.tripleInfo=new ArrayList<PODataList>();
		this.dc=new DepthCollector();
		this.edgeCount=0;
		
		this.explicitSameAsList=new ArrayList<String>();
		this.existInUriStorage=false;
		this.validity=-1;
		
	}


	

	public int getValidity() {
		return validity;
	}


	public void setValidity(int validity) {
		this.validity = validity;
	}


	public boolean isExistInUriStorage() {
		return existInUriStorage;
	}


	public void setExistInUriStorage(boolean existInUriStorage) {
		this.existInUriStorage = existInUriStorage;
	}


	public ArrayList<String> getExplicitSameAsList() {
		return explicitSameAsList;
	}


	public void setExplicitSameAsList(ArrayList<String> explicitSameAsList) {
		this.explicitSameAsList = explicitSameAsList;
	}


	public DepthCollector getDc() {
		return dc;
	}


	public void setDc(DepthCollector dc) {
		this.dc = dc;
	}


	public int getEdgeCount() {
		return edgeCount;
	}


	public void setEdgeCount(int edgeCount) {
		this.edgeCount = edgeCount;
	}


	public Hashtable<Integer, Entity> getSameAsList() {
		return sameAsList;
	}





	public void setSameAsList(Hashtable<Integer, Entity> sameAsList) {
		this.sameAsList = sameAsList;
	}





	public Hashtable<String, String> getDuplicationList() {
		return duplicationList;
	}





	public void setDuplicationList(Hashtable<String, String> duplicationList) {
		this.duplicationList = duplicationList;
	}



/*

	public ArrayList<ArrayList<Entity>> getAllCycleList() {
		return allCycleList;
	}





	public void setAllCycleList(ArrayList<ArrayList<Entity>> allCycleList) {
		this.allCycleList = allCycleList;
	}

*/



	public int getIndex() {
		return index;
	}





	public void setIndex(int index) {
		this.index = index;
	}





	public int getVisitFlag() {
		return visitFlag;
	}





	public void setVisitFlag(int visitFlag) {
		this.visitFlag = visitFlag;
	}








	public double getEntityConfidence() {
		return entityConfidence;
	}





	public void setEntityConfidence(double entityConfidence) {
		this.entityConfidence = entityConfidence;
	}


/*


	public ArrayList<Double> getPriviousConfidence() {
		return priviousConfidence;
	}





	public void setPriviousConfidence(ArrayList<Double> priviousConfidence) {
		this.priviousConfidence = priviousConfidence;
	}


*/






	public String getUri() {
		return uri;
	}





	public void setUri(String uri) {
		this.uri = uri;
	}





	public String getSparqlEndPoint() {
		return sparqlEndPoint;
	}





	public void setSparqlEndPoint(String sparqlEndPoint) {
		this.sparqlEndPoint = sparqlEndPoint;
	}





	public int getDepth() {
		return depth;
	}





	public void setDepth(int depth) {
		this.depth = depth;
	}





	public int getSearchflag() {
		return searchflag;
	}





	public void setSearchflag(int flag) {
		this.searchflag = flag;
	}





	public String getSurfaceSearchUri() {
		return surfaceSearchUri;
	}





	public void setSurfaceSearchUri(String surfaceSearchUri) {
		this.surfaceSearchUri = surfaceSearchUri;
	}





	public String getParentUri() {
		return parentUri;
	}





	public void setParentUri(String parentUri) {
		this.parentUri = parentUri;
	}





	public boolean inputSameAsLink(int p,Entity entity){
	
		
		Enumeration e=this.sameAsList.elements();
		
		while(e.hasMoreElements())
		{
			Entity tmp=(Entity)e.nextElement();
			if(tmp==entity)
			{
				return false;
			}
		}
		
		this.sameAsList.put(p,entity);
		return true;
		
	}


	public ArrayList<PODataList> getTripleInfo() {
		return tripleInfo;
	}


	public void setTripleInfo(ArrayList<PODataList> tripleInfo) {
		this.tripleInfo = tripleInfo;
	}
	
	
	
}
