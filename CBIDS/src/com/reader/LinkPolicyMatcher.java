package com.reader;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import com.data.Entity;
import com.data.PODataList;
import com.data.PredicateMatchingInfo;

public class LinkPolicyMatcher implements Matcher{
	/**
	 * Matcher match sourceLOD's Link-Policy with targetLOD's Link-Policy 
	 */

	private Model linkPolicy;
	private String sourceLOD;
	private String sourceType;
	private String targetLOD;
	private String sourceTypePredicate;
	
	private String targetTypePredicate;
	private String targetType;
	private ArrayList<PredicateMatchingInfo> predicateMatchinginfo;
	private ArrayList<String> linkpolicyTargetPredicateList;
	
	private int matchflag;

	private int num;
	@Override
	public void setLinkPolicy(Model linkPolicy) {
		// TODO Auto-generated method stub
		this.linkPolicy=linkPolicy;
		this.matchflag=0;
		this.num=0;
	}

	
	@Override
	public void setTargetLOD(String targetLOD) {
		// TODO Auto-generated method stub
		this.targetLOD=targetLOD;
		
		
	}
	
	@Override 
	public void setPredicateMatchiginfo()
	{
		this.predicateMatchinginfo=new ArrayList<PredicateMatchingInfo>();
		
	}
	

	@Override 
	public void setLinkpolicyTargetPredicateList()
	{
		this.linkpolicyTargetPredicateList=new ArrayList<String>();
		
	}
	
	@Override
	public boolean checkTypeRestrction(ArrayList<PODataList> uriResult, Entity entity)
	{
		
		this.sourceLOD=entity.getSparqlEndPoint();
		
		ArrayList<String> typePredicateList=getTypePredicateList(entity.getSparqlEndPoint());
		//get sourceLOD's predicates that discript topic registried in Link-Policy.
		
		for(int i=0; i<uriResult.size();i++ )
		{
			
		     for(int p=0; p<typePredicateList.size(); p++)
				{
					if(uriResult.get(i).getPredicate().equals(typePredicateList.get(p)))
					{
						if(searchTypeRestriction(
								uriResult.get(i).getObject(),uriResult.get(i).getPredicate(),entity.getSparqlEndPoint()))
						{
							//if find predicate that describe topic, check topic in Link-Policy.
							return true;
						}
					}
					}
		}
		return false;
	}
	
	@Override
	public void matchPredicate(String sourcePredicate,String sourceObject)
	{
		
		for(int i=0; i<this.predicateMatchinginfo.size(); i++)
		{
			Enumeration<String> tmp=this.predicateMatchinginfo.get(i).getSourcePredicate().elements();
			
			while(tmp.hasMoreElements())
			{
				String s=tmp.nextElement();
				
				if(s.equals(sourcePredicate))
				{
					this.predicateMatchinginfo.get(i).addObject(sourceObject);
				}
			}
		}
		
	}
	
	public void addPredicateMatchingInfo(
			String sourcePredicate,String sourceObject,String targetPredicate)
	{
		//save objects described by predicate matched and targetLOD's predicates connected with that.
		for(int i=0; i<this.predicateMatchinginfo.size(); i++)
		{
			if(sourcePredicate.equals(this.predicateMatchinginfo.get(i).getSourcePredicate()))
			{
				this.predicateMatchinginfo.get(i).addTargetPredicate(targetPredicate);
				this.predicateMatchinginfo.get(i).addObject(sourceObject);
				return;
			}
		}
		this.predicateMatchinginfo.add(new PredicateMatchingInfo(sourcePredicate,sourceObject,targetPredicate));
		return;
	}
	
	
	public void getTargetTypeRestrcition(String sourceType)
	{
		//get Topic Restriction Specification about targetLOD matched with 'sourceType'.
		String query="select ?o5 ?o6 where { <"+this.sourceLOD+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o2."
				+"?o2 <http://linkpolicy.org/ontology/sourceRistricType> ?o3. FILTER regex(?o3,\""+sourceType+"\")"
				+"?o2 <http://linkpolicy.org/ontology/targetRestric> ?o4."
				+"?o4 <http://linkpolicy.org/ontology/targetRistricPredicate> ?o5."
				+"?o4 <http://linkpolicy.org/ontology/targetRistricType> ?o6}";
		
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode targetTypePredicate=soln.get("?o5");
		      RDFNode targetType=soln.get("?o6");
		    }
		 qexec.close();
	}
	
	public ArrayList<String> getTypePredicateList(String uri)
	{
		//get predicates describe topic
		ArrayList<String> typePredicateList=new ArrayList<String>();
		String query="select ?o3 where {<"+uri+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o2. ?o2 <http://linkpolicy.org/ontology/sourceRestricPredicate> ?o3}";
		
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode typePredicate=soln.get("?o3");
		      typePredicateList.add(typePredicate.toString());
		    }
		 qexec.close();
		 
		 return typePredicateList;
	}
	
	public boolean searchTypeRestriction(
			String type,String typePredicate,String sourceUri)
	{
		//get Topic Restriction Specification about targetLOD matched with 'type'.
		int flag=0;
			String query="select ?o5 ?o7 ?o8 where {<"+sourceUri+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
			+"?o <http://linkpolicy.org/ontology/targetLOD> ?o2 FILTER regex(?o2,\""+this.targetLOD+"\")"
			+ "?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o3."
			+ "?o3 <http://linkpolicy.org/ontology/sourceRestricPredicate> ?o4. FILTER regex(?o4,\""+typePredicate+"\")"
			+ "?o3 <http://linkpolicy.org/ontology/sourceRistricType> ?o5."
			+ "?o3 <http://linkpolicy.org/ontology/targetRestric> ?o6."
			+ "?o6 <http://linkpolicy.org/ontology/targetRistricPredicate> ?o7."
			+ "?o6 <http://linkpolicy.org/ontology/targetRistricType> ?o8}";
			QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
			ResultSet results = qexec.execSelect();
			
			for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode sourceType=soln.get("?o5");
		      RDFNode targetTypePredicate=soln.get("?o7");
		      RDFNode targetType=soln.get("?o8");
		      
		      if(type.equals(sourceType.toString()))
		      {
		    	  //get target LOD's topic and predicate 
		    	  this.sourceType=sourceType.toString();
		    	  this.targetTypePredicate=targetTypePredicate.toString();
		    	  this.targetType=targetType.toString();
		    	  flag=1;
		      }
		      
		    }
			
			if(flag==1)
			{
				return true;
			}
			
		return false;
	}
	public ArrayList<String> getTargetList(String uri)//소스LOD의 타겟 LOD탐색
	{

		//get target LOD list linked with source LOD
		ArrayList<String> targetList=new ArrayList<String>();
		
		String query="select ?o2 where {<"+uri+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/targetLOD> ?o2}";
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
		
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode targetLOD=soln.get("?o2");
		      targetList.add(targetLOD.toString());
		    }
		 qexec.close();
		
		return targetList;
	}
	
	public String getTargetTypePredicate() {
		return targetTypePredicate;
	}
	public String getTargetType() {
		return targetType;
	}
	
	public ArrayList<PredicateMatchingInfo> getpredicateMatchinginfo() {
		return predicateMatchinginfo;
	}

	
	public ArrayList<String> getLinkpolicyTargetPredicateList(){
		return linkpolicyTargetPredicateList;
	}
	
	public boolean checkPredicateCount()
	{
		if(this.predicateMatchinginfo.size()==this.num)
			return true;
		
		return false;
	}
	
	public void test()
	{
		String query="select (COUNT(?a) AS ?count) where { <"+this.sourceLOD+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/targetLOD> ?o2. FILTER regex(?o2,\""+targetLOD+"\")"
				+"?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o3."
				+"?o3 <http://linkpolicy.org/ontology/sourceRistricType> ?o4 FILTER regex(?o4,\""+this.sourceType+"\")"
				+"?o3 <http://linkpolicy.org/ontology/preidcateMatching> ?o5}";
		
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
		for ( ; results.hasNext() ; )
	    {
	      QuerySolution soln = results.nextSolution() ;
	      RDFNode sourceType=soln.get("?count");
	}
	}
	
	
	@Override
	public void makeLinkPolicyPredicateList()
	{
		
		String query="select ?o6 ?o7 ?o8 where { <"+this.sourceLOD+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/targetLOD> ?o2. FILTER regex(?o2,\""+this.targetLOD+"\")"
				+"?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o3."
				+"?o3 <http://linkpolicy.org/ontology/sourceRistricType> ?o4 FILTER regex(?o4,\""+this.sourceType+"\")"
				+"?o3 <http://linkpolicy.org/ontology/preidcateMatching> ?o5."
				+"?o5 <http://linkpolicy.org/ontology/sourcePredicate> ?o6."
				+"?o5 <http://linkpolicy.org/ontology/targetPredicate> ?o7."
				+"?o5 <http://linkpolicy.org/ontology/predicateType> ?o8}";
		
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
	
		int flag=0;
		Hashtable<String,String> tmp=new Hashtable<String,String>();
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode matchedSourcePredicate=soln.get("?o6");
		      RDFNode matchedTargetPredicate=soln.get("?o7");
		      RDFNode predicateType=soln.get("?o8");
		    
		      if(predicateMatchinginfo.isEmpty())
		      {
		    	 
			    this.predicateMatchinginfo.add(new PredicateMatchingInfo(matchedSourcePredicate.toString(),matchedTargetPredicate.toString()));
		    	  continue;
		      }
		      
		      for(int i=0; i<this.predicateMatchinginfo.size(); i++)
		      {
		    	  
		    	  if(this.predicateMatchinginfo.get(i).getTargetPredicateList().containsKey(matchedTargetPredicate.toString()))
		    	  {
		    		  this.predicateMatchinginfo.get(i).getSourcePredicate().put(matchedSourcePredicate.toString(), matchedSourcePredicate.toString());
		    		  flag++;
		    	  }
		    	  else if(this.predicateMatchinginfo.get(i).getSourcePredicate().containsKey(matchedSourcePredicate.toString()))
		    	  {
		    		  this.predicateMatchinginfo.get(i).getTargetPredicateList().put(matchedTargetPredicate.toString(),matchedTargetPredicate.toString());
		    		  flag++;
		    	  }
		      }
		      
		      if(flag==0)
		      {
		    	  
	
			    	 this.predicateMatchinginfo.add(new PredicateMatchingInfo(matchedSourcePredicate.toString(),matchedTargetPredicate.toString()));

		    	  
		      }
		      
		      flag=0;
		    }
		
		 qexec.close();
		 tmp.clear();
		 this.matchflag++;
		
	}


	
}
