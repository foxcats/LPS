package com.comparison;

import java.util.ArrayList;

import com.data.Entity;
import com.data.PredicateMatchingInfo;
import com.data.TargetModel;

public interface Comparison {

	public void compareWithCandidate(Entity entity,ArrayList<PredicateMatchingInfo> predicateMatchinginfo, TargetModel targetCandidate, String sparqlEndpoint, int depth, String surfaceSearchUri,
			String parentURI,double similiarty);

	

}
