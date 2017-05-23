package com.constructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.data.PredicateMatchingInfo;
import com.data.TargetModel;
import com.searcher.Search;
import com.searcher.Searcher;

public class CandidateConstructor implements Constructor{

	/**
	 * CandidateConstructor make candidates in targetLODs that have topic and predicates matched with targetLOD's LinkPolicy 
	 */

	

	private HashSet<String> uriList;
	private ArrayList<TargetModel> targetModelList;
	
	public CandidateConstructor()
	{
		this.targetModelList=new ArrayList<TargetModel>();
	}
	
	public TargetModel searchCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD)
	{
		//CandidateConstructor save candidate entities, because these entities can be used again
		for(int i=0; i<targetModelList.size(); i++)
		{
			if(targetModelList.get(i).getTargetLOD().equals(targetLOD)) 
				//if In-depth searching is performed by tagetLODs again searched before, 
				//CandidateConstructor return candidate entities saved in targetMoelList
			{
				System.out.println("find candidate");
				return targetModelList.get(i);
			}
		}
		System.out.println("Can't find candidate,start searching");
		return makeCandidate(targetType,targetTypePredicate,predicateMatchinginfo,targetLOD);
	}
	
	public TargetModel makeCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD)
	{
		//Search candidate entities 
		ArrayList<org.apache.jena.query.ResultSet> resultList=new ArrayList<org.apache.jena.query.ResultSet>();
		Search searcher=new Searcher();
		String query;
		int offset=0;
		
		query=makeSPARQL(targetType,targetTypePredicate,predicateMatchinginfo);

		for(;;)
		{
		
			String query2=query;
			query2=sparql(query2,offset);
			
			
			org.apache.jena.query.ResultSet results=searcher.executeQuery(query2, targetLOD);
			if(results==null)
			{
				break;
			}
			
			if(!results.hasNext())
			{
				break;
			}
			resultList.add(results);
			
		offset+=1000;
		}
		
		Model tmpModel=extractCandidate(resultList);
		TargetModel targetModel=new TargetModel(targetType,tmpModel,targetLOD);
		this.targetModelList.add(targetModel);
		searcher.close();
		return targetModel;
	}
	
	public String makeSPARQL(String targetType,String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		
		//make sparql use topic and predicates mached in targetLOD's Link-Policy
		String query="";
		
		
		ArrayList<String> tmp=new ArrayList<String>();
		
		for(int i=0; i<predicateMatchinginfo.size(); i++)
		{
			
			Collection<String> col=predicateMatchinginfo.get(i).getTargetPredicateList().values();
			Iterator itr=col.iterator();
			
			while(itr.hasNext())
			{
				tmp.add(itr.next().toString());
			}
		}
		
		
		query="select * where{?s <"+targetTypePredicate+"> <"+targetType+">. {?s <"+tmp.get(0)+"> ?o}";
		
		for(int k=1; k<tmp.size(); k++)
		{
			query+=" UNION {?s <"+tmp.get(k)+"> ?o}";
		}
		
		query+="?s ?p ?o FILTER (?p=<"+tmp.get(0)+">";
		
		for(int p=1; p<tmp.size(); p++)
		{
			query+="|| ?p=<"+tmp.get(p)+">";
		}
		
		query+=")}";
		System.out.println(query);
		return query;
	}
	
	public String sparql(String query, int offset)
	{
		String sparqlQuery=query;
		sparqlQuery+=" LIMIT 1000 OFFSET "+offset;
		return sparqlQuery;
	}
	
	public Model extractCandidate(ArrayList<org.apache.jena.query.ResultSet> tmp)
	{
		//extract candidate entities subject used in objectComparison
		this.uriList=new HashSet<String>(); 
		Model model = ModelFactory.createDefaultModel();
		String queryString="select distinct ?s where{?s ?p ?o}";
		
		Query query = QueryFactory.create(queryString);
		int num=0;
		for(int i=0; i<tmp.size(); i++){	    
		 for ( ; tmp.get(i).hasNext() ; )
			   
		 {
			      QuerySolution soln = tmp.get(i).nextSolution() ;
			      Resource subject=soln.getResource("?s");
			      Resource predicate=soln.getResource("?p");
			      RDFNode object=soln.get("?o");
			      Property p=model.createProperty(predicate.toString());
			      model.createResource(subject.toString()).addProperty(p, object);
			      num++;
		 }
		}
		
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		ResultSet results = qexec.execSelect();
	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      Resource subject2=soln.getResource("?s");
		      this.uriList.add(subject2.toString());
		    }
		
		 
		 qexec.close();
		 return model;
		//System.out.println(uriList.size());
		
	}
	
	public  HashSet<String> getList()
	{
		return this.uriList;
	}

	
	
}
