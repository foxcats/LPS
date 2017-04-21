package IDS;

import org.apache.jena.rdf.model.Model;

import Queue.Qnode;

public interface IDSearcher {

	
	public void search();
	public void setQnode(Qnode qNode);
	public void setLinkPolicy(Model linkPolicy);
	public void setSimilarity(int similarity);
}
