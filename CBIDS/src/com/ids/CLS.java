package com.ids;

import com.comparison.Comparison;
import com.constructor.Constructor;
import com.data.Entity;
import com.reader.LinkPolicyMatcher;
import com.reader.LinkPolicyReader;
import com.stack.Stack;

public class CLS implements IDS{

	private int depth;
	private double similarity;

	private IDS lps;
	private IDS els;
	
	public CLS(IDS lps,IDS els)
	{
		this.lps=lps;
		this.els=els;
	}
	
	
	@Override
	public void search(Entity source) {
		// TODO Auto-generated method stub
		
		this.lps.setDepth(this.depth);
		this.els.setDepth(this.depth);
		
		lps.search(source);
		els.search(source);
		
	}

	@Override
	public LinkPolicyReader getReader() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setReader(LinkPolicyReader reader) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getDepth() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setDepth(int depth) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public double getSimilarity() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setSimilarity(double similarity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void search(Entity source, double percent) {
		// TODO Auto-generated method stub
		
	}

}
