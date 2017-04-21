package IDS;

import java.util.ArrayList;

import Data.PredicateMatchingInfo;
import Data.TargetModel;

public interface Constructor {

	public TargetModel makeCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD);
	
	public TargetModel searchCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD);
}
