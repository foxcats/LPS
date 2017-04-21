package IDS;

public class IDSFactory {

	
	
	public IDSearcher getFLS()
	{
		Matcher linkPolicyMatcher=new LinkPolicyMatcher();
		Constructor candidateConstructor=new CandidateConstructor();
		ObjectComparison objectComparison=new ObjectComparison();
		return new FLS(linkPolicyMatcher, candidateConstructor,objectComparison);
	}
	
	public IDSearcher getELS()
	{
		return new ELS();
		
	}
	
	public IDSearcher getCLS()
	{
		
		return new CLS(getFLS(),getELS());
	}
}
