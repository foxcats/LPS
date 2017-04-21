package IDS;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;

import Data.PredicateMatchingInfo;
import Data.TargetModel;
import Data.TripleInformation;
import Queue.Qnode;

public class FLS implements IDSearcher{

	
	public Matcher linkPolicyMatcher;
	public Constructor candidateConstructor;
	public Comparison objectComparison;
	
	public Qnode qNode;
	public Model linkPolicy;//소스LOD에 해당하는 연결정책 저장
	public int similarity;
	
	public void setQnode(Qnode qNode)
	{
		this.qNode=qNode;
	}
	public void setLinkPolicy(Model linkPolicy)
	{
		this.linkPolicy=linkPolicy;
	}
	
	public void setSimilarity(int similarity)
	{
		this.similarity=similarity;
	}
	
	public FLS(Matcher linkPolicyMatcher,Constructor candidateConstructor,Comparison objectComparison)
	{
		this.linkPolicyMatcher=linkPolicyMatcher;
		this.candidateConstructor=candidateConstructor;
		this.objectComparison=objectComparison;
	}
	
	@Override
	public void search() {
		// TODO Auto-generated method stub
		TargetModel targetCandidate=null;
		
		linkPolicyMatcher.setLinkPolicy(linkPolicy);
		
		//타입제한이 존재하는지 확인
		
		ArrayList<String> targetList=linkPolicyMatcher.getTargetList(qNode.getData().getSparqlEndpoint());	
		String parentString=qNode.getData().getUri();
		
		
		ArrayList<PredicateMatchingInfo> predicateMatchinginfo=null;
		for(int i=0; i<targetList.size(); i++)
		{
			//this.targetType=new ArrayList<String>();
			//this.targetTypePredicate=new ArrayList<String>();
			
			linkPolicyMatcher.setTargetLOD(targetList.get(i));
			
			if(!linkPolicyMatcher.checkTypeRestrction(qNode))
			{
				System.out.println("형식제한 매칭 실패");
				continue;
			}
			
			linkPolicyMatcher.setPredicateMatchiginfo();
			
			for(int k=0; k<TripleInformation.getList().size(); k++)
			{
				linkPolicyMatcher.matchPredicate(TripleInformation.getList().get(k).getPredicate(),TripleInformation.getList().get(k).getObject(),targetList.get(i));
			}			
				// TODO Auto-generated method stub
			
			predicateMatchinginfo=linkPolicyMatcher.getpredicateMatchinginfo();
			targetCandidate=candidateConstructor.searchCandidate(linkPolicyMatcher.getTargetType(),linkPolicyMatcher.getTargetTypePredicate(),
					linkPolicyMatcher.getpredicateMatchinginfo(),targetList.get(i));
			
			
			this.objectComparison.compareWithCandidate(predicateMatchinginfo,targetCandidate,targetList.get(i), qNode.getData().getDepth(), 
					qNode.getData().getsurfaceSearchUri(),parentString,similarity);
			
			
		}
	}

}
