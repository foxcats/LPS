package com.ids;

import com.comparison.ObjectComparison;
import com.constructor.CandidateConstructor;
import com.reader.LinkPolicyReader;
import com.stack.EntityStack;
import com.stack.Stack;

public class IDSFactory {
	
	/**
	 * IDSFactory return LPS ELS and CLS
	 * CLS perform LPS and ELS at the same time
	 * @param similarity
	 * @return
	 */
	
	public IDS getLPS(LinkPolicyReader linkPolicy,double similarity)
	{
		IDS ids=new LPS(linkPolicy,(1-similarity),new CandidateConstructor(),new ObjectComparison());
		return ids;
	}

	public IDS getELS(double percent)
	{
		IDS ids=new ELS(percent);
		return ids;
	}
	
	public IDS getCLS(LinkPolicyReader linkPolicy,double similarity,double percent)
	{
		IDS ids=new CLS(getLPS(linkPolicy,similarity),getELS(percent));
		return ids;
	}
}
