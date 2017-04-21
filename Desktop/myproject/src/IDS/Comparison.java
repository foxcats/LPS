package IDS;

import java.util.ArrayList;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import Data.PredicateMatchingInfo;
import Data.TargetModel;

public interface Comparison {

	public void compareWithCandidate(ArrayList<PredicateMatchingInfo> predicateMatchinginfo, TargetModel targetCandidate, String sparqlEndpoint, int depth, String surfaceSearchUri,
			String parentURI,int similiarty);

	

}
