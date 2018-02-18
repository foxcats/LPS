package com.ids;

import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.jena.rdf.model.Model;

import com.comparison.Comparison;
import com.constructor.Constructor;
import com.data.Entity;
import com.data.PODataList;
import com.data.PredicateMatchingInfo;
import com.data.TargetModel;
import com.reader.LinkPolicyMatcher;
import com.reader.LinkPolicyReader;

public class LPS implements IDS{

	private LinkPolicyReader reader;
	private int depth;
	private double similarity;
	private LinkPolicyMatcher linkPolicyMatcher;
	public Constructor candidateConstructor;
	public Comparison objectComparison;
	
	public LPS( LinkPolicyReader reader,double similarity,Constructor candidateConstructor,Comparison objectComparison)
	{
		this.reader=reader;
		this.similarity=similarity;
		this.linkPolicyMatcher=new LinkPolicyMatcher();
		this.candidateConstructor=candidateConstructor;
		this.objectComparison=objectComparison;
	}

	@Override
	public LinkPolicyReader getReader() {
		return reader;
	}

	@Override
	public void setReader(LinkPolicyReader reader) {
		this.reader = reader;
	}

	@Override
	public int getDepth() {
		return depth;
	}

	@Override
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public double getSimilarity() {
		return similarity;
	}

	@Override
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	@Override
	public void search(Entity source) {
		
		Model linkPolicy=reader.getLinkPolicy(source);
		
		if(linkPolicy==null)
		{
			System.out.println("no linkPolicy");
			return;
		}
		// TODO Auto-generated method stub
		TargetModel targetCandidate=null;
		
		linkPolicyMatcher.setLinkPolicy(linkPolicy);//Set sourceLOD's Link-Policy
		
	
		
		ArrayList<String> targetList=linkPolicyMatcher.getTargetList(source.getSparqlEndPoint());	
		//get target LOD List from Link-Policy linked with sourceLOD saved in Qnode 
		
		String parentString=source.getUri();
		
		
		ArrayList<PredicateMatchingInfo> predicateMatchinginfo=null;
		
		ArrayList<PODataList> uriResult=null;
		
		for(int i=0; i<targetList.size(); i++)
		{
			
			linkPolicyMatcher.setTargetLOD(targetList.get(i));
			ArrayList<String> linkpolicyTargetPredicateList=new ArrayList<String>();

			uriResult=source.getTripleInfo();//get Source's triple info
			
			if(uriResult==null)
			{
				System.out.println("null");
				return;
			}
			
			if(!linkPolicyMatcher.checkTypeRestrction(uriResult,source))
			{
				System.out.println("Fail Topic Restriction Specification");
				continue;
			}
		
			//check Topic Restriction Specification if this qNode's entity have topic registried in Link-Policy
			
			linkPolicyMatcher.setPredicateMatchiginfo();
			linkPolicyMatcher.setLinkpolicyTargetPredicateList();
			linkPolicyMatcher.makeLinkPolicyPredicateList();
			

			
			for(int k=0; k<uriResult.size(); k++ )
			{
				 linkPolicyMatcher.matchPredicate(uriResult.get(k).getPredicate(),uriResult.get(k).getObject());

			}
			
			//match predicate about sourceLOD using Link-Policy*/
			
			predicateMatchinginfo=linkPolicyMatcher.getpredicateMatchinginfo();
			
			if(predicateMatchinginfo.size()<2)
			{
				return;
			}
				
			//linkpolicyTargetPredicateList=linkPolicyMatcher.getLinkpolicyTargetPredicateList();
			//get targetLOD's predicate registried in Link-Policy and matched with sourceLOD's predicate
			//System.out.println("predicateInfo 사이즈: "+linkpolicyTargetPredicateList.size()+" ");
			
			
			for(int p=0; p<predicateMatchinginfo.size(); p++ )
			{
				Enumeration<String> tmp=predicateMatchinginfo.get(p).getTargetPredicateList().elements();
				
				while(tmp.hasMoreElements()){
					String tmpTarget=tmp.nextElement();
					System.out.println(tmpTarget);
					linkpolicyTargetPredicateList.add(tmpTarget);
				}
				
			}
			
			System.out.println(linkpolicyTargetPredicateList.size());
			
			targetCandidate=candidateConstructor.searchCandidate(linkPolicyMatcher.getTargetType(),linkPolicyMatcher.getTargetTypePredicate(),
					linkpolicyTargetPredicateList,targetList.get(i));
			//search candidate entities that have topic and predicate matched with targetLOD's LinkPolicy in targetLOD
			
		    this.objectComparison.compareWithCandidate(source,predicateMatchinginfo,targetCandidate,targetList.get(i), source.getDepth(), 
		    		source.getSurfaceSearchUri(),parentString,similarity);
			//compare object with sourceLOD and targetLOD's entities 
			
		}
		
	}

	@Override
	public void search(Entity source, double percent) {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
