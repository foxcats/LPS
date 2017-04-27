package com.example;

import com.ids.IDSFactory;
import com.ids.IDScontroller;
import com.linkpolicy.LinkPolicyReader;

public class Main {
	
	public static void main(String[] args){
		
	
		
		LinkPolicyReader linkpolicyReader=new LinkPolicyReader();
	
		linkpolicyReader.readLinkPolicy("ko.dbpedia.org_linkPolicy");
		// Read Link-Policy File
		
		IDScontroller IDS =new IDScontroller(linkpolicyReader,"http://ko.dbpedia.org/sparql", 
				"select * where { ?s rdfs:label ?o FILTER regex(str(?o),\"아이언맨\")}",2,0);
		//make class of IDScontroller (LinkPolicyReader,Source-SPARQL EndPoint,SPARQL Query,Depth,Similarity)
		IDS.searchingInLOD(new IDSFactory().getCLS());
		//Searching start(get Class from IDSFactory)
	}

}
