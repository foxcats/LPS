package com.ids;

import com.comparison.ObjectComparison;
import com.constructor.CandidateConstructor;
import com.constructor.Constructor;
import com.matcher.LinkPolicyMatcher;
import com.matcher.Matcher;

public class IDSFactory {

	/**
	You can get the searching measures from IDSFactory.
	Method(getLPS,getELS,getCLS) in IDSFactory is used to class of IDSController
	**/
	
	public IDSearcher getLPS()
	{
		Matcher linkPolicyMatcher=new LinkPolicyMatcher();//matching Link-policy btween sourceLOD and targetLOD
		Constructor candidateConstructor=new CandidateConstructor();//make candidate from targetLOD
		ObjectComparison objectComparison=new ObjectComparison();//Compare object btween sourceLOD and targetLOD
		return new LPS(linkPolicyMatcher, candidateConstructor,objectComparison);
	}
	//LPS(Link-Policy-Searching, using onliy Link-Policy)
	
	public IDSearcher getELS()
	{
		return new ELS();
		
	}
	//ELS(Explicit-Link-Searching, using only Explicit-Link)
	public IDSearcher getCLS()
	{
		
		return new CLS(getLPS(),getELS());
	}
	//ELS+LPS
}
