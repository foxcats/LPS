package com.ids;

import com.comparison.ObjectComparison;
import com.constructor.CandidateConstructor;
import com.constructor.Constructor;
import com.matcher.LinkPolicyMatcher;
import com.matcher.Matcher;

public class IDSFactory {

	
	
	public IDSearcher getLPS()
	{
		Matcher linkPolicyMatcher=new LinkPolicyMatcher();
		Constructor candidateConstructor=new CandidateConstructor();
		ObjectComparison objectComparison=new ObjectComparison();
		return new LPS(linkPolicyMatcher, candidateConstructor,objectComparison);
	}
	
	public IDSearcher getELS()
	{
		return new ELS();
		
	}
	
	public IDSearcher getCLS()
	{
		
		return new CLS(getLPS(),getELS());
	}
}
