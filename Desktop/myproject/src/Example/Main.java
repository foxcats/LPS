package Example;

import IDS.IDSFactory;
import IDS.IDScontroller;
import LinkPolicy.LinkPolicyReader;

public class Main {
	
	public static void main(String[] args){
		
	
		LinkPolicyReader linkpolicyReader=new LinkPolicyReader();
	
		linkpolicyReader.readLinkPolicy("ko.dbpedia.org_linkPolcy");
		
		IDScontroller IDS =new IDScontroller(linkpolicyReader,"http://ko.dbpedia.org/sparql", 
				"select * where { ?s rdfs:label ?o FILTER regex(str(?o),\"아이언맨\")}",2,0);
		
		IDS.searchingInLOD(new IDSFactory().getCLS());
	}

}
