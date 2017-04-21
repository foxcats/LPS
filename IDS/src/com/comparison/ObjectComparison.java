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
import com.ids.ObjectSimilarity;
import com.stringhandler.StringFunction;

public class ObjectComparison implements Comparison{
	
	
	private ArrayList<String> finalResult;
	private double subjectScore;
	
	public void compareWithCandidate(ArrayList<PredicateMatchingInfo> predicateMatchinginfo,TargetModel targetCandidate,
			String sparqlEndpoint, int depth, String surfaceSearchUri,String parentURI,int similiarty) 
	{
		// TODO Auto-generated method stub
		
		this.subjectScore=0;
		this.finalResult=new ArrayList<String>();
		
		ResultSet tmpResult=null;
		
		
		ArrayList<String> targetSubject=getTargetSubject(targetCandidate);
		
		for(int i=0 ; i<targetSubject.size(); i++)
		{
			String tmpStr=targetSubject.get(i);
			String queryString=makeSparql(tmpStr);
			
			Query query = QueryFactory.create(queryString);
			QueryExecution qexec = QueryExecutionFactory.create(query,targetCandidate.getTargetCandidateModel());
			tmpResult = qexec.execSelect();
			
			
			if(tmpResult==null)
			{
				System.out.println(tmpStr+" 결과 NULL");
				continue;
				
			}
			
		    double score=matchLinkPolicy(tmpResult,predicateMatchinginfo);
		
		    if(score<0)
		    {
		    	continue;
		    }
		    compareScore(tmpStr,score,similiarty);
		}
	
		for(int k=0; k<finalResult.size(); k++)
		{
			System.out.println("비교결과: "+finalResult.get(k));
			IDScontroller.IDSQueue.enQueue(new Uri(finalResult.get(k),sparqlEndpoint,depth-1,
					surfaceSearchUri,1,parentURI));
			
		}
		
		finalResult.clear();
		
	}
	
	public void compareScore(String subject,double score,int similiarty)
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
		     
		     //System.out.println(predicate.toString());
		     for(int k=0; k<predicateMatchinginfo.size(); k++)
		     {
		    	 if(predicateMatchinginfo.get(k).getTargetPredicateList().get(predicate.toString()) != null)
		    	 {
		    		 
		    		 if(predicateMatchinginfo.get(k).getObjectList().isEmpty())
		    		 {
		    			 System.out.println("목적어없음");
		    			 continue;
		    		 }
		    		 
		    		 //System.out.println("dd");
		    		 compareObject(predicateMatchinginfo.get(k),object.toString());
		    		 predicateMatchinginfo.get(k).setTargetPredicateflag(1);
		    		 flag++;
		   
		    	 }
		     }
		     
		   //System.out.println(tmpInfo.getPredicate()+" "+tmpInfo.getObjectList().size()+ " / "+predicate.toString()+" / "+object.toString());
		     
		    
		     
		 }
		
		
		
		if(flag==0)
		{
			return -1;
		}
		
		for(int p=0; p<predicateMatchinginfo.size(); p++)
		{
			if(predicateMatchinginfo.get(p).getTagetPredicateflag()==0)
			{
				//System.out.println("탈락");
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
		double score=0;
		double finalScore=0;
		double tmpScore=0;
		
		int num=0;
		

		for(int i=0; i<predicateMatchinginfo.size(); i++)
		{
			PredicateMatchingInfo tmpPrInfo=predicateMatchinginfo.get(i);
			
			if(tmpPrInfo.getScoreList()==null||tmpPrInfo.getScoreList().isEmpty())
			{
				//System.out.println("스코어없음");
				continue;
			}
			
			for(int k=0; k<tmpPrInfo.getScoreList().size(); k++)
			{
				//System.out.println("점수"+k+" "+tmpPrInfo.getScoreList().get(k));
			
				if(k==0)
				{
					finalScore=tmpPrInfo.getScoreList().get(k);
					//System.out.println("체크");
					continue;
				}
				
				tmpScore=tmpPrInfo.getScoreList().get(k);
				
				if(tmpScore<finalScore)
					finalScore=tmpScore;
				
				
			}
			
			score+=finalScore;
			num++;
		 }
		 
		
		
		//System.out.println(score+" "+score/num+" "+num+"\n");
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
