package com.writer;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.data.PODataList;
import com.data.TripleInformation;
import com.queue.Qnode;
import com.searcher.Search;
import com.searcher.Searcher;

public class TripleAccumulator {
	
private writer rdfWriter;
	
	public TripleAccumulator()
	{
		rdfWriter=new RDFWriter();
	}
	
	public void writeRDF(Qnode node)
	{
		TripleInformation.getList().clear();
		TripleInformation.getSameAsList().clear();
		int count=0;
		
		if(node.getData().getUri().equals(node.getData().getsurfaceSearchUri()))
		{
			rdfWriter.makeSubject(node.getData().getUri());
		}
		
		
		Search searcher=new Searcher();
		
		ResultSet sparqlResult=searcher.executeQuery(makeSparql(node), node.getData().getSparqlEndpoint());
		
		if(sparqlResult==null)
		{
			return;
		}
		
		for( ; sparqlResult.hasNext(); )
		{
			 QuerySolution soln = sparqlResult.nextSolution() ;
		     Resource predicate = soln.getResource("?p") ;
		     RDFNode object= soln.get("?o");
		     
		     if(predicate.toString().equals("http://www.w3.org/2002/07/owl#sameAs"))
		    	  //sameAs에 해당하는 URI를 저장한다.
		      {
		    	 TripleInformation.getSameAsList().add(object.toString());
		      }
		     
		     
		     rdfWriter.makeRDF(node.getData().getsurfaceSearchUri(),predicate.toString(), object.toString());
		     //System.out.println(predicate.toString()+" -----"+object.toString());
		     
		     TripleInformation.getList().add(new PODataList(predicate.toString(),object.toString()));
		     count++;
		  }
		

	    searcher.close();
		
	   // System.out.println("sameAsSize: "+stateInformation.getSameAsList().size()+"\n");
		
	}
	
	
	public String makeSparql(Qnode node) 
	{
		String Uri=node.getData().getUri();
		
		String queryString=
				"select *"+
					" where {<"+Uri+"> ?p ?o }";
		
		return queryString;
					
	}
	
	public void writeFile()
	{
		rdfWriter.writeFile();
	}
	
	public Model getModel()
	{
		rdfWriter.getModel().write(System.out);
		return rdfWriter.getModel();
	}

}
