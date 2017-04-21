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

	
	//select * where{?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Film>. {?s <http://fr.dbpedia.org/property/titreOriginal> ?o} UNION {?s <http://fr.dbpedia.org/property/commonsTitre> ?o} UNION {?s <http://fr.dbpedia.org/property/titre> ?o} UNION {?s <http://fr.dbpedia.org/property/annéeDeSortie> ?o} UNION {?s <http://fr.dbpedia.org/property/année> ?o}?s ?p ?o FILTER (?p=<http://fr.dbpedia.org/property/titreOriginal>|| ?p=<http://fr.dbpedia.org/property/commonsTitre>|| ?p=<http://fr.dbpedia.org/property/titre>|| ?p=<http://fr.dbpedia.org/property/annéeDeSortie>|| ?p=<http://fr.dbpedia.org/property/année>)}


	private HashSet<String> uriList;
	private ArrayList<TargetModel> targetModelList;
	
	public CandidateConstructor()
	{
		this.targetModelList=new ArrayList<TargetModel>();
	}
	
	public TargetModel searchCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD)
	{
		for(int i=0; i<targetModelList.size(); i++)
		{
			if(targetModelList.get(i).getTargetLOD().equals(targetLOD))
			{
				System.out.println("후보군 발견");
				return targetModelList.get(i);
			}
		}
		System.out.println("후보군 미발견 검색 시작");
		return makeCandidate(targetType,targetTypePredicate,predicateMatchinginfo,targetLOD);
	}
	
	public TargetModel makeCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD)
	{
		ArrayList<org.apache.jena.query.ResultSet> resultList=new ArrayList<org.apache.jena.query.ResultSet>();
		Search searcher=new Searcher();
		String query;
		int offset=0;
		//Iterator itr=stateInformation.getSourceLabelList().iterator();
		
		query=makeSPARQL2(targetType,targetTypePredicate,predicateMatchinginfo);

		for(;;)
		{
		
			String query2=query;
			//String label=itr.next().toString();
			//System.out.println(label);
			query2=sparql(query2,offset);
			//System.out.println(query2);
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
		
		Model tmpModel=makeCandidate(resultList);
		TargetModel targetModel=new TargetModel(targetType,tmpModel,targetLOD);
		this.targetModelList.add(targetModel);
		searcher.close();
		return targetModel;
	}
	
	public String makeSPARQL2(String targetType,String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		
		
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
	
	public Model makeCandidate(ArrayList<org.apache.jena.query.ResultSet> tmp)
	{
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
