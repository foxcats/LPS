package com.reader;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;

import com.data.Entity;
import com.data.PODataList;
import com.data.PredicateMatchingInfo;

public interface Matcher {



	void setLinkPolicy(Model linkPolicy);

	ArrayList<String> getTargetList(String sparqlEndpoint);

	
	void setTargetLOD(String targetLOD);
	String getTargetTypePredicate();
	String getTargetType();
	ArrayList<PredicateMatchingInfo> getpredicateMatchinginfo();
	 void setPredicateMatchiginfo();

	void setLinkpolicyTargetPredicateList();

	ArrayList<String> getLinkpolicyTargetPredicateList();

	boolean checkPredicateCount();

	void test();


	void makeLinkPolicyPredicateList();

	void matchPredicate(String sourcePredicate, String sourceObject);


	boolean checkTypeRestrction(ArrayList<PODataList> uriResult, Entity entity);
}
