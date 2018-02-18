package com.ids;

import com.data.Entity;
import com.reader.LinkPolicyReader;
import com.stack.Stack;

public interface IDS {
	
	
	public void search(Entity source);
	public LinkPolicyReader getReader();
	public void setReader(LinkPolicyReader reader);
	public int getDepth();
	public void setDepth(int depth);
	public double getSimilarity();
	public void setSimilarity(double similarity);
	public void search(Entity source,double percent) ;

	
}
