package com.comparison;

import java.util.ArrayList;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import com.data.PredicateMatchingInfo;
import com.data.TargetModel;

public interface Comparison {

	public void compareWithCandidate(ArrayList<PredicateMatchingInfo> predicateMatchinginfo, TargetModel targetCandidate, String sparqlEndpoint, int depth, String surfaceSearchUri,
			String parentURI,double similiarty);

	

}
