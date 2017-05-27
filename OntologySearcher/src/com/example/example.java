package com.example;

import com.os.OntologySearcher;

public class example {

	
	public void main(String[] args)
	{
		//'LODInfo' file used for searching ontology info must be in project folder. 'LODInfo' is produced by IDS   
		OntologySearcher os=new OntologySearcher();
		os.searchOntologyInfo("fileName");
	}
}
