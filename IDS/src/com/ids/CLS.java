package com.ids;

import org.apache.jena.rdf.model.Model;

import com.queue.Qnode;

public class CLS implements IDSearcher{

	private IDSearcher fls;
	private IDSearcher els;
	
	private Qnode qNode;
	private Model linkPolicy;
	private int similarity;
	
	public CLS(IDSearcher fls,IDSearcher els)
	{
		this.fls=fls;
		this.els=els;
	}

	@Override
	public void search() {
		// TODO Auto-generated method stub
		fls.setLinkPolicy(linkPolicy);
		fls.setQnode(qNode);
		fls.setSimilarity(similarity);
		fls.search();
		
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
	public void setSimilarity(int similarity) {
		// TODO Auto-generated method stub
		this.similarity=similarity;
	}

}
