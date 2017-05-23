package com.ids;

import org.apache.jena.rdf.model.Model;

import com.queue.Qnode;

public interface IDSearcher {

	
	public void search();
	public void setQnode(Qnode qNode);
	public void setLinkPolicy(Model linkPolicy);
	public void setSimilarity(double similarity);
}
