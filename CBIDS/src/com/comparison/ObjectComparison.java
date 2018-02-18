package com.comparison;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.controller.PESController;
import com.data.Entity;
import com.data.PredicateMatchingInfo;
import com.data.TargetModel;
import com.stringhandler.StringFunction;

public class ObjectComparison implements Comparison{
	
	/**
	 * ObjectComparison compare object between source entity and target entity
	 * and is scored by similarity
	 * similarity has range of similarity 0~1 and low similarity is more similar than high similarity with object
	 */
	private ArrayList<String> finalResult;
	
	public void compareWithCandidate(Entity source, ArrayList<PredicateMatchingInfo> predicateMatchinginfo, TargetModel targetCandidate,
			String sparqlEndpoint, int depth, String surfaceSearchUri,String parentURI,double similarity) 
	{
		// TODO Auto-generated method stub
		
		this.finalResult=new ArrayList<String>();
		
		ResultSet queryResults=null;
		
		
		ArrayList<String> targetSubject=getTargetSubject(targetCandidate); 
		//Get target Entities Uri form targetCandidate(TargetModel)
		
		
		for(int i=0 ; i<targetSubject.size(); i++)
		{
			
			String candidate=targetSubject.get(i);
			String queryString=makeSparql(candidate);
			//select * where { <uri> ?p ?o}
			
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query,targetCandidate.getTargetCandidateModel());
			
			//find uri info in Candidate-Model,using suject saved in targetSubject-List
			queryResults = qexec.execSelect();
			
			
			if(queryResults==null)
			{
				continue;
			}
			
		    double score=matchLinkPolicy(queryResults,predicateMatchinginfo);
		    
		    
		    if(score<0)
		    {
		    	continue;
		    }
		    setScore(candidate,score,similarity);
		    //if score is more than similarity that suggested in IDScontrolloer at first, save score in 'finalResult'
		}
	
		
		int p=source.getSameAsList().size();
		
		for(int k=0; k<finalResult.size(); k++)
		{
			if(source.getUri()==finalResult.get(k))
				//if target URI is same with source URI, Don't register in sameAsList
				continue;
			
			System.out.println("    Comparison Result: "+finalResult.get(k)+" in " + surfaceSearchUri);
			
			Entity cr=PESController.uriStorage.getEntity(finalResult.get(k));
			
			if(cr==null){
				Entity targetEntity=new Entity(finalResult.get(k),sparqlEndpoint,source.getDepth()-1,
						source.getSurfaceSearchUri(),2,source.getUri());
				
				source.getSameAsList().put(p, targetEntity);
				source.getDuplicationList().put(finalResult.get(k), finalResult.get(k));
				targetEntity.setExistInUriStorage(true);
				PESController.uriStorage.putEntity(targetEntity.getUri(), targetEntity);
				p++;
			}
			else
			{
				if(source.getDuplicationList().get(finalResult.get(k))==null)
				{
					source.getSameAsList().put(p,cr);
					source.getDuplicationList().put(finalResult.get(k), finalResult.get(k));
					p++;
				}
				else
					continue;
			}
			
		}
		//compare score and decide uri that have the highest score;
		
		finalResult.clear();
		
	}
	
	public void setScore(String subject,double score,double similarity )
	{	
		
		if(score<=similarity)// if score is lower than similarity, insert finalResult 
		{
			this.finalResult.add(subject);
		}
	}

	
	public String makeSparql(String uri) 
	{
		
		ArrayList<String> tmp=new ArrayList<String>();
	
		String query=
				"select *"+
					" where {<"+uri+"> ?p ?o }";
	
		return query;
					
	}
	
	public ArrayList<String> getTargetSubject(TargetModel targetCandidate)
	{
		//extract candidate entities's subject in targetCondidate Model
		ArrayList<String> targetSubject=new ArrayList<String>();
		String queryString="select distinct ?s  where { ?s ?p ?o}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, targetCandidate.getTargetCandidateModel());
		ResultSet tmpResult = qexec.execSelect();
		
		for(; tmpResult.hasNext() ;)
		{
			 QuerySolution soln = tmpResult.nextSolution() ;
		     Resource subject=soln.getResource("?s");
		     targetSubject.add(subject.toString());
		}
		return targetSubject;
	}
	
	
	
	public double matchLinkPolicy(ResultSet policyResultSet, ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		//using 'PredicateMatchingInfo' that have source entity's object and targetLOD's predicate
		//In this function, it search predicate and object info in policyResultSet that is candidate entitis's info.
		//Also, compare object described by targetLOD's predicate with source entity's object in PredicateMatchingInfo
		//All Scores compared about object are saved in 'scoreList' of 'PredicateMatchingInfo and averaged 
		
		setTargetPredicateFlag(predicateMatchinginfo);
		setScoreList(predicateMatchinginfo);
		int flag=0;
		String test="";
		StringFunction stringFunc=new StringFunction();
		
		for( ; policyResultSet.hasNext(); )
		{
			
			 QuerySolution soln =policyResultSet.nextSolution();
			 Resource predicate = soln.getResource("?p") ;
		     RDFNode object= soln.get("?o");
		     
		     
		     for(int k=0; k<predicateMatchinginfo.size(); k++)
		     {
		    	
		    	 if(predicateMatchinginfo.get(k).getTargetPredicateList().get(predicate.toString()) != null)
		    		 //target predicate have matched predicate that is regitered in Link-Policy
		    	 {
		    		 
		    		 if(predicateMatchinginfo.get(k).getObjectList().isEmpty())
		    			 // predicate don't have object
		    		 {
		    			 continue;
		    		 }
		    		 
		    		 
		    		 
		    		 compareObject(predicateMatchinginfo.get(k),object.toString());
		    		 //compare object similarity
		    		 
		    		 predicateMatchinginfo.get(k).setTargetPredicateflag(1);
		    		 //set targetPredicateFlag 1
		    		 flag++;
		   
		    	 }
		     }
		     
		    
		     
		 }
		
		
		
		if(flag==0)
		{
			return -1;
		}
		
		for(int p=0; p<predicateMatchinginfo.size(); p++)
		{
			if(predicateMatchinginfo.get(p).getTagetPredicateflag()==0)
			{
				return -1;
			}
		}
		// if targetPredicate's flag is 0, Target Entity doesn't have enough matching predicate that is registered in Link-Policy
		// all targetPredicate's flag must be 1
		
		double score=getScore(predicateMatchinginfo);
		return score;
		
		
	}
	
	public void setTargetPredicateFlag(ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		
		for(int i=0; i< predicateMatchinginfo.size(); i++)
		{
			 predicateMatchinginfo.get(i).setTargetPredicateflag(0);
		}
	}
	
	public void compareObject(PredicateMatchingInfo srcInfo,String targetObject)
	{
		//Compare target entity's object with source entity's object.
		//score is saved  in 'scoreList' of 'PredicateMatchingInfo'
		// 1 is the lowest score 0 is the highest score
		// srcInfo has object list is composed by source predicate's object 
		
		ObjectSimilarity objectSimilarity=new ObjectSimilarity();
		
		double score=0;
		double tmpScore=0;
		int flag=0;// flag 0 is first object
		
		for(int i=0; i<srcInfo.getObjectList().size(); i++)
		{    
			
			if(srcInfo.getObjectList().get(i).contains("integer"))
			{
				if(makeNumeric(srcInfo.getObjectList().get(i)).length()<=2)
					//Don't perform Object Comparison when numeric Objects length is fewer than  numeric 2
				{
					if(flag==1)
					{
						continue;
					}
					
					score=1;
					continue;
				}
			}
			
			if(flag==0)//calculate first object score
			{
				
				score=objectSimilarity.calculateSimilarity(srcInfo.getObjectList().get(i),targetObject);
				flag++;
				
				continue;
			}
			
			
			
			tmpScore=objectSimilarity.calculateSimilarity(srcInfo.getObjectList().get(i),targetObject);		
			//calculate object score
			
			if(tmpScore<score)
			{
				score=tmpScore;
			}
	
			
		}
		srcInfo.getScoreList().add(score);
		//add score calculated in scoreList 
	}
	
	public double getScore(ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		//Average all scores in 'scoreList' of 'PredicateMatchingInfo'
		double score=0;
		double finalScore=0;
		double tmpScore=0;
		
		int num=0;
		

		for(int i=0; i<predicateMatchinginfo.size(); i++)
		{
			PredicateMatchingInfo tmpPrInfo=predicateMatchinginfo.get(i);
			
			if(tmpPrInfo.getScoreList()==null || tmpPrInfo.getScoreList().isEmpty())
			{
				continue;
			}
			
			for(int k=0; k<tmpPrInfo.getScoreList().size(); k++)
			{
			
				if(k==0)
				{
					finalScore=tmpPrInfo.getScoreList().get(k);
					continue;
				}
				
				tmpScore=tmpPrInfo.getScoreList().get(k);
				
				if(tmpScore<finalScore)
					finalScore=tmpScore;
				
				
			}
			
			score+=finalScore;
			num++;
		 }
	
		score=score/num;
		return score;
	}

	
	public void setScoreList(ArrayList<PredicateMatchingInfo> predicateMatchinginfo)
	{
		
		for(int i=0; i<predicateMatchinginfo.size(); i++)
		{
			predicateMatchinginfo.get(i).setScoreList();
		}
		
	}
	
	boolean isNumeric(String s) {
		  try {
		      Double.parseDouble(s);
		      return true;
		  } catch(NumberFormatException e) {
		      return false;
		  }
		}
	
	public String makeNumeric(String s){
		StringTokenizer st = new StringTokenizer(s,"^^"); 
		
		String numeric=st.nextToken();
		return numeric;

	}

}
