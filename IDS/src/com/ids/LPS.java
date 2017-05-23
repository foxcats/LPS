package com.ids;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;

import com.comparison.Comparison;
import com.constructor.Constructor;
import com.data.PredicateMatchingInfo;
import com.data.TargetModel;
import com.data.TripleInformation;
import com.matcher.Matcher;
import com.queue.Qnode;

public class LPS implements IDSearcher{

	/**
	 * LPS perform in-depth searching,using Link-Policy of target LODs that is registered in Link-Policy of source LOD
	 */
	
	public Matcher linkPolicyMatcher;
	public Constructor candidateConstructor;
	public Comparison objectComparison;
	
	public Qnode qNode;
	public Model linkPolicy;//Link-Policy of souce LOD
	public double similarity;
	
	public void setQnode(Qnode qNode)
	{
		this.qNode=qNode;
	}
	public void setLinkPolicy(Model linkPolicy)
	{
		this.linkPolicy=linkPolicy;
	}
	
	public void setSimilarity(double similarity)
	{
		this.similarity=similarity;
	}
	
	public LPS(Matcher linkPolicyMatcher,Constructor candidateConstructor,Comparison objectComparison)
	{
		this.linkPolicyMatcher=linkPolicyMatcher;
		this.candidateConstructor=candidateConstructor;
		this.objectComparison=objectComparison;
		
		//set linkPolicyMatcher, candidateConstructor and objectComparison
	}
	
	@Override
	public void search() {
		// TODO Auto-generated method stub
		TargetModel targetCandidate=null;
		
		linkPolicyMatcher.setLinkPolicy(linkPolicy);//Set sourceLOD's Link-Policy
		
	
		
		ArrayList<String> targetList=linkPolicyMatcher.getTargetList(qNode.getData().getSparqlEndpoint());	
		//get target LOD List from Link-Policy linked with sourceLOD saved in Qnode 
		
		String parentString=qNode.getData().getUri();
		
		
		ArrayList<PredicateMatchingInfo> predicateMatchinginfo=null;
		for(int i=0; i<targetList.size(); i++)
		{
			
			linkPolicyMatcher.setTargetLOD(targetList.get(i));
			
			if(!linkPolicyMatcher.checkTypeRestrction(qNode))
			{
				System.out.println("Fail Topic Restriction Specification");
				continue;
			}
			
			//check Topic Restriction Specification if this qNode's entity have topic registried in Link-Policy
			
			linkPolicyMatcher.setPredicateMatchiginfo();
			
			for(int k=0; k<TripleInformation.getList().size(); k++)
			{
				linkPolicyMatcher.matchPredicate(TripleInformation.getList().get(k).getPredicate(),TripleInformation.getList().get(k).getObject(),targetList.get(i));
			}			
			//match predicate about sourceLOD using Link-Policy
			
			predicateMatchinginfo=linkPolicyMatcher.getpredicateMatchinginfo();
			//get targetLOD's predicate registried in Link-Policy and matched with sourceLOD's predicate
			
			targetCandidate=candidateConstructor.searchCandidate(linkPolicyMatcher.getTargetType(),linkPolicyMatcher.getTargetTypePredicate(),
					linkPolicyMatcher.getpredicateMatchinginfo(),targetList.get(i));
			//search candidate entities that have topic and predicate matched with targetLOD's LinkPolicy in targetLOD
			
			this.objectComparison.compareWithCandidate(predicateMatchinginfo,targetCandidate,targetList.get(i), qNode.getData().getDepth(), 
					qNode.getData().getsurfaceSearchUri(),parentString,similarity);
			//compare object with sourceLOD and targetLOD's entities 
			
		}
	}

}
