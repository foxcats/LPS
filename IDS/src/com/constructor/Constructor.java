package com.constructor;

import java.util.ArrayList;

import com.data.PredicateMatchingInfo;
import com.data.TargetModel;

public interface Constructor {

	public TargetModel makeCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD);
	
	public TargetModel searchCandidate(String targetType,
			String targetTypePredicate,ArrayList<PredicateMatchingInfo> predicateMatchinginfo,String targetLOD);
}
