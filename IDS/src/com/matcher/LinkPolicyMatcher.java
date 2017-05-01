package com.matcher;

import java.util.ArrayList;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import com.data.PredicateMatchingInfo;
import com.data.TripleInformation;
import com.queue.Qnode;

public class LinkPolicyMatcher implements Matcher{

	private Model linkPolicy;
	private String sourceLOD;
	private String sourceType;
	private String targetLOD;
	private String sourceTypePredicate;
	
	private String targetTypePredicate;
	private String targetType;
	private ArrayList<PredicateMatchingInfo> predicateMatchinginfo;
	
	
	
	@Override
	public void setLinkPolicy(Model linkPolicy) {
		// TODO Auto-generated method stub
		this.linkPolicy=linkPolicy;
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
	public boolean checkTypeRestrction(Qnode qNode)
	{
		
		this.sourceLOD=qNode.getData().getSparqlEndpoint();
		
		ArrayList<String> typePredicateList=getTypePredicateList(qNode.getData().getSparqlEndpoint());
		//get sourceLOD's predicates that discript topic registried in Link-Policy.
		
		for(int k=0; k<TripleInformation.getList().size(); k++)
		{
			for(int p=0; p<typePredicateList.size(); p++)
			{
				if(TripleInformation.getList().get(k).getPredicate().equals(typePredicateList.get(p)))
				{
					if(searchTypeRestriction(
							TripleInformation.getList().get(k).getObject(),TripleInformation.getList().get(k).getPredicate(),qNode.getData().getSparqlEndpoint()))
					{
						//if find predicate that describe topic, check topic in Link-Policy.
						
						System.out.println("find topic matched");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public void matchPredicate(String sourcePredicate,String sourceObject,String targetLOD)
	{
		
		String query="select ?o6 ?o7 where { <"+this.sourceLOD+"> <http://linkpolicy.org/ontology/linkpolicy> ?o."
				+"?o <http://linkpolicy.org/ontology/targetLOD> ?o2. FILTER regex(?o2,\""+targetLOD+"\")"
				+"?o <http://linkpolicy.org/ontology/restrictPredicateMatch> ?o3."
				+"?o3 <http://linkpolicy.org/ontology/sourceRistricType> ?o4 FILTER regex(?o4,\""+this.sourceType+"\")"
				+"?o3 <http://linkpolicy.org/ontology/preidcateMatching> ?o5."
				+"?o5 <http://linkpolicy.org/ontology/sourcePredicate> ?o6."
				+"?o5 <http://linkpolicy.org/ontology/targetPredicate> ?o7}";
		
		QueryExecution qexec = QueryExecutionFactory.create(query, linkPolicy);
		ResultSet results = qexec.execSelect();
	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode matchedSourcePredicate=soln.get("?o6");
		      RDFNode matchedTargetPredicate=soln.get("?o7");
		      if(sourcePredicate.equals(matchedSourcePredicate.toString()))
		      {
			   //if there are source entity's predicates matched by Link-Policy, save target LOD's predicate connected with them.
		    	  addPredicateMatchingInfo(sourcePredicate,sourceObject,matchedTargetPredicate.toString());
		      }
		    }
		 qexec.close();
		
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
		      System.out.println(targetTypePredicate.toString()+" "+targetType.toString());
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
			//System.out.println(query);
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
	public ArrayList<String> getTargetList(String uri)
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

	
	
}
