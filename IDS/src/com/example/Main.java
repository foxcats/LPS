package com.example;

import com.ids.IDSFactory;
import com.ids.IDScontroller;
import com.linkpolicy.LinkPolicyReader;

public class Main {
	
	public static void main(String[] args){
		
	
		LinkPolicyReader linkpolicyReader=new LinkPolicyReader();
		// Read Link-Policy File
		
		linkpolicyReader.readLinkPolicy("ko.dbpedia.org_linkPolicy");
		//make class of IDScontroller (LinkPolicyReader,Source-SPARQL EndPoint,SPARQL Query,Depth,Similarity)
		
		IDScontroller IDS =new IDScontroller("IronMan_3_1",linkpolicyReader,"http://ko.dbpedia.org/sparql", 
				"select * where { ?s rdfs:label ?o FILTER regex(str(?o),\"아이언맨\")}",2,0.9);
		
		IDS.searchingInLOD(new IDSFactory().getLPS());
		//Searching start(get Class from IDSFactory)
	}

}
