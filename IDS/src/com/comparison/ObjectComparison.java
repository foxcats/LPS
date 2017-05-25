package com.comparison;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.data.PredicateMatchingInfo;
import com.data.TargetModel;
import com.data.Uri;
import com.ids.IDScontroller;
import com.stringhandler.StringFunction;

public class ObjectComparison implements Comparison{
	
	/**
	 * ObjectComparison compare object between source entity and target entity
	 * and is scored by similarity
	 */
	private ArrayList<String> finalResult;
	private double subjectScore;
	
	public void compareWithCandidate(ArrayList<PredicateMatchingInfo> predicateMatchinginfo,TargetModel targetCandidate,
			String sparqlEndpoint, int depth, String surfaceSearchUri,String parentURI,double similarity) 
	{
		// TODO Auto-generated method stub
		
		this.subjectScore=0;
		this.finalResult=new ArrayList<String>();
		
		ResultSet tmpResult=null;
		
		
		ArrayList<String> targetSubject=getTargetSubject(targetCandidate);
		
		//using candidate entities to compare object
		for(int i=0 ; i<targetSubject.size(); i++)
		{
			String tmpStr=targetSubject.get(i);
			String queryString=makeSparql(tmpStr);
			
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query,targetCandidate.getTargetCandidateModel());
			
			//find uri info in Candidate-Model,using suject saved in targetSubject-List
			tmpResult = qexec.execSelect();
			
			
			if(tmpResult==null)
			{
				System.out.println(tmpStr+"Result is NULL");
				continue;
				
			}
			
		    double score=matchLinkPolicy(tmpResult,predicateMatchinginfo);
		    //scored by similarity
		    
		    if(score<0)
		    {
		    	continue;
		    }
		    setScore(tmpStr,score,similarity);
		    //if score is more than similarity that suggested in IDScontrolloer at first, save score in 'finalResult'
		}
	
		for(int k=0; k<finalResult.size(); k++)
		{
			System.out.println("Comparison Result: "+finalResult.get(k));
			IDScontroller.IDSQueue.enQueue(new Uri(finalResult.get(k),sparqlEndpoint,depth-1,
					surfaceSearchUri,1,parentURI));
			
		}
		//compare score and decide uri that have the highest score;
		
		finalResult.clear();
		
	}
	
	public void setScore(String subject,double score,double similiarty)
	{
		
		if(score<=similiarty)
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
		    	 {
		    		 
		    		 if(predicateMatchinginfo.get(k).getObjectList().isEmpty())
		    		 {
		    			 System.out.println("There are no object");
		    			 continue;
		    		 }
		    		 
		    		 compareObject(predicateMatchinginfo.get(k),object.toString());
		    		 predicateMatchinginfo.get(k).setTargetPredicateflag(1);
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
		
		ObjectSimilarity objectSimilarity=new ObjectSimilarity();
		
		double score=0;
		double tmpScore=0;
		int flag=0;
		
		for(int i=0; i<srcInfo.getObjectList().size(); i++)
		{    
			if(flag==0)
			{
				
				score=objectSimilarity.calculateSimilarity(srcInfo.getObjectList().get(i),targetObject);
				flag++;
				
				continue;
			}
			
			tmpScore=objectSimilarity.calculateSimilarity(srcInfo.getObjectList().get(i),targetObject);
			
			if(tmpScore<score)
			{
				score=tmpScore;
			}
	
			
		}
		srcInfo.getScoreList().add(score);
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
			
			if(tmpPrInfo.getScoreList()==null||tmpPrInfo.getScoreList().isEmpty())
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

}
