package com.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.InvalidPropertyURIException;

import com.data.PODataList;
import com.data.TripleInformation;
import com.queue.Qnode;
import com.searcher.Search;
import com.searcher.Searcher;

public class TripleAccumulator {
	
 /**
  *  Accumulator accumulate URI info searched by in-depth searching, dequeuing in Queue
  */
	
	private writer rdfWriter;
	
	public TripleAccumulator()
	{
		rdfWriter=new RDFWriter();
	}
	
	public void writeRDF(Qnode node)
	{
		
		//write URI info searched in target LOD, using URI that is saved in Queue node dequeued
		
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
		     {
		    	 TripleInformation.getSameAsList().add(object.toString());
		     }
		     //save sameAs info used for ELS
		     
		     
		     rdfWriter.makeRDF(node.getData().getsurfaceSearchUri(),predicate.toString(), object.toString());
		     
		     
		     TripleInformation.getList().add(new PODataList(predicate.toString(),object.toString()));
		     count++;
		  }
		//search URI info in target LOD that is registered in Queue node dequeued
		

	    searcher.close();
		
	}
	
	
	public String makeSparql(Qnode node) 
	{
		String Uri=node.getData().getUri();
		
		String queryString=
				"select *"+
					" where {<"+Uri+"> ?p ?o }";
		
		return queryString;
					
	}
	
	public void writeFile(String filename)
	{
		rdfWriter.writeFile(filename);
	}
	
	public Model getModel()
	{
		rdfWriter.getModel().write(System.out);
		return rdfWriter.getModel();
	}
	
	public void writeTargetLOD(HashSet<String> targetLODs,String sourceLOD)
	{
		Model model=ModelFactory.createDefaultModel();
		Resource subject=model.createResource(sourceLOD);
		Property predicate=model.createProperty("http://linkpolicy.org/ontology/targetLOD");
		
		Iterator<String> itr=targetLODs.iterator();
		
		while(itr.hasNext())
		{
			String targetLOD=itr.next();
			subject.addProperty(predicate, targetLOD);
		}
		
		 FileWriter out = null;
			try {
				
				out = new FileWriter("LODInfo.rdf");

				model.write(out,"N-TRIPLES");
				
				out.close();
				System.out.println("write LOD");
			} catch (IOException e) {
				
			}
			catch(InvalidPropertyURIException e)
			{
				System.out.println("writing Error");
			}
	}

}
