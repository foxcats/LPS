package com.example;

import java.util.ArrayList;

import com.controller.IDSController;
import com.data.Entity;
import com.ids.IDSFactory;
import com.reader.LinkPolicyReader;
import com.searcher.Search;
import com.searcher.SparqlFactory;
import com.sfs.SFS;

public class Main {

	public static void main(String[] args)
	{
		LinkPolicyReader linkpolicyReader=new LinkPolicyReader();
		IDSFactory idsFactory=new IDSFactory();
		linkpolicyReader.readLinkPolicy("ko.dbpedia.org_linkPolicy");
 
		Search searcher=new SparqlFactory().getSearcher();
		
		SFS sfs=new SFS("select * where { ?s rdfs:label ?o FILTER regex(str(?o),\"아이언맨\")}","http://ko.dbpedia.org/sparql",searcher);
		ArrayList<Entity> sfsList=sfs.searchSfs();
		//search surface-searching
		
	
		//IDSController IDS =new IDSController("IronMan_3_1",sfsList,6,1,idsFactory.getELS(1));
		//IDSController IDS =new IDSController("IronMan_3_1",sfsList,13,1,idsFactory.getELS(0.8));
		IDSController IDS =new IDSController("IronMan_3_1",sfsList,2,1,idsFactory.getLPS(linkpolicyReader,1));
		//insert information about file name, sfs results, depth, Entity Confidence(EC) and In-depth Searching(IDS)
		//IDS: ELS(Explicit Link-based Searching) LPS(Link Policy-based Searching) CLS(ELS+LPS)
		IDS.search();
		//start IDS
	}
}