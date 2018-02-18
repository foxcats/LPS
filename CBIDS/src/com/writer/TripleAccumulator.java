package com.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

import com.controller.PESController;
import com.data.Entity;
import com.data.PODataList;
import com.data.TripleInformation;
import com.filter.UriFilter;
import com.searcher.Search;
import com.searcher.Searcher;

public class TripleAccumulator {
	
 /**
  *  Accumulator accumulate URI info searched by in-depth searching, dequeuing in Queue
  */
	
	private writer rdfWriter;
	private Search searcher;
	private UriFilter filter;
	
	public TripleAccumulator(Search searcher,writer rdfWriter,UriFilter filter,String subject)
	{
		this.rdfWriter= rdfWriter;
		this.searcher=searcher;
		this.filter=filter;
		rdfWriter.makeSubject(subject);

	}
	
	public TripleAccumulator(Search searcher,writer rdfWriter,UriFilter filter)
	{
		this.rdfWriter= rdfWriter;
		this.searcher=searcher;
		this.filter=filter;
		

	}
	
	public void makeTestSubject(String subject){
		rdfWriter.getModel().removeAll();
		rdfWriter.makeSubject(subject);
	}
	
	public boolean writeRDF(Entity source)
	{
		
		//write URI Triples searched in target LOD
		
		TripleInformation.getList().clear();
		TripleInformation.getSameAsList().clear();
		
		ArrayList<PODataList> preObList=null;
		ResultSet sparqlResult=null;
		Search searcher=null;
		int count=0;
		
		
		preObList=PESController.uriStorage.getEntity(source.getUri()).getTripleInfo();
		
		
		int index=0;
		
		
		if(preObList.isEmpty())
		{
			searcher=new Searcher();
			sparqlResult=searcher.executeQuery(makeSparql(source), source.getSparqlEndPoint());
			
			if(sparqlResult!=null){
			for( ; sparqlResult.hasNext(); )
			{
				 QuerySolution soln = sparqlResult.nextSolution() ;
			     Resource predicate = soln.getResource("?p") ;
			     RDFNode object= soln.get("?o");
			     
			     //save sameAs info used for ELS
			     
			     if(predicate.toString().equals("http://www.w3.org/2002/07/owl#sameAs"))
			     {
			    	 TripleInformation.getSameAsList().add(object.toString());
			    	 
			    	 if(this.filter.checkValidity(object.toString(),source))
			    	 {
			    		 source.getExplicitSameAsList().add(object.toString());
			    		
			    	 }
			    	 
			    	 continue;
			     }
			     
			     if(predicate.toString().equals("http://dbpedia.org/ontology/wikiPageWikiLink"))
			     {
			    	 continue;
			     }
			     
			     preObList.add(new PODataList(predicate.toString(),object.toString()));
			     rdfWriter.makeRDF(source.getSurfaceSearchUri(),predicate.toString(), object.toString());
			     
			     count++;
			}
			rdfWriter.showNum();
			source.setValidity(1);
			PESController.uriStorage.getEntity(source.getUri()).setTripleInfo(preObList);
			}
			searcher.close();
			System.out.println("트리플등록완료");
			return true;
			
		}
		return true;
	}
	
	public boolean writeRDFTest(Entity source){
		
		//write URI Triples searched in target LOD
		
				TripleInformation.getList().clear();
				TripleInformation.getSameAsList().clear();
				
				ArrayList<PODataList> preObList=null;
				ResultSet sparqlResult=null;
				Search searcher=null;
				int count=0;
				
				
				preObList=PESController.uriStorage.getEntity(source.getUri()).getTripleInfo();
				
				
				int index=0;
				
				
				if(preObList.isEmpty())
				{
					//System.out.println("트리플정보없음");
					searcher=new Searcher();
					sparqlResult=searcher.executeQuery(makeSparql(source), source.getSparqlEndPoint());
				/*
					if(sparqlResult==null)
					{
						PESController.preventLOD.put(source.getSparqlEndPoint(),source.getSparqlEndPoint());
						searcher.close();
						return false;
					}*/
					if(sparqlResult!=null){
					for( ; sparqlResult.hasNext(); )
					{
						 QuerySolution soln = sparqlResult.nextSolution() ;
					     Resource predicate = soln.getResource("?p") ;
					     RDFNode object= soln.get("?o");
					     
					     //save sameAs info used for ELS
					    // TripleInformation.getList().add(new PODataList(predicate.toString(),object.toString()));
				

					     if(predicate.toString().equals("http://www.w3.org/2002/07/owl#sameAs"))
					     {
					    	 if(object.toString().contains("http://uk.dbpedia.org"))
					    	 {
					    		 continue;
					    	 }
					    	 
					    	 TripleInformation.getSameAsList().add(object.toString());
					    	 
					    	 if(this.filter.checkValidity(object.toString(),source))
					    	 {
					    		 source.getExplicitSameAsList().add(object.toString());
					    		// System.out.println("필터체크완료");
					    	 }
					    	 
					    	 continue;
					     }
					     
					     if(predicate.toString().equals("http://dbpedia.org/ontology/wikiPageWikiLink"))
					     {
					    	 continue;
					     }
					     
					     preObList.add(new PODataList(predicate.toString(),object.toString()));
					     rdfWriter.makeRDF(source.getSurfaceSearchUri(),predicate.toString(), object.toString());
					     
					     count++;
					}
					//rdfWriter.showNum();
					source.setValidity(1);
					PESController.uriStorage.getEntity(source.getUri()).setTripleInfo(preObList);
					//this.savedList.add(source);
					}
					searcher.close();
					//System.out.println("트리플등록완료");
					return true;
					
				}
				else{
					
					if(source.getValidity()!=1){
						
						for(int i=0; i<preObList.size(); i++){
							rdfWriter.makeRDF(source.getSurfaceSearchUri(),preObList.get(i).getPredicate(), preObList.get(i).getObject());
						}
						//rdfWriter.showNum();
						source.setValidity(1);
						//System.out.println("트리플등록완료2");
					}
				}
				
				
				return true;
	}
	
	public String makeSparql(Entity entity) 
	{
		String Uri= entity.getUri();
		
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
