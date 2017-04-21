package IDS;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import Data.PredicateMatchingInfo;
import Queue.Qnode;

public interface Matcher {


	boolean checkTypeRestrction(Qnode qNode);

	void setLinkPolicy(Model linkPolicy);

	ArrayList<String> getTargetList(String sparqlEndpoint);

	void matchPredicate(String predicate, String string, String string2);
	
	void setTargetLOD(String targetLOD);
	String getTargetTypePredicate();
	String getTargetType();
	ArrayList<PredicateMatchingInfo> getpredicateMatchinginfo();
	 void setPredicateMatchiginfo();
}
