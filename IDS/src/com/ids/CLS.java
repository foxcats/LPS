package com.ids;

import org.apache.jena.rdf.model.Model;

import com.queue.Qnode;

public class CLS implements IDSearcher{

	/**
	 * CLS perform LPS and EPS
	 */
	private IDSearcher lps;
	private IDSearcher els;
	
	private Qnode qNode;
	private Model linkPolicy;
	private double similarity;
	
	public CLS(IDSearcher fls,IDSearcher els)
	{
		this.lps=fls;
		this.els=els;
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		lps.setLinkPolicy(linkPolicy);
		lps.setQnode(qNode);
		lps.setSimilarity(similarity);
		lps.search();
		
		els.setQnode(qNode);
		els.search();
		
	}

	@Override
	public void setQnode(Qnode qNode) {
		// TODO Auto-generated method stub
		
		this.qNode=qNode;
		
	}

	@Override
	public void setLinkPolicy(Model linkPolicy) {
		// TODO Auto-generated method stub
		this.linkPolicy=linkPolicy;
		
	}

	@Override
	public void setSimilarity(double similarity) {
		// TODO Auto-generated method stub
		this.similarity=similarity;
	}

}
