package com.os;

import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import com.searcher.Searcher;

public class OntologySearcher {
	
	/**
	 * OntologySearcher search Ontology info about predicate used in result of IDS by two methods
	 * first method use target LOD address searched in results of IDS to search ontology
	 * second method use new LOD address generated by predicate. 
	 */

	private Model resultModel;
	private Model LODInfo;
	private ArrayList<String> predicateList;
	private ArrayList<String> targetLODList;
	private Model ontologyModel;
	
	public void readIdsResults(String fileName)
	{
		 Model model = ModelFactory.createDefaultModel();

		 model.read(fileName+".rdf");
		 this.resultModel=model;
		
		 model=ModelFactory.createDefaultModel();
		 model.read("LODInfo.rdf");
		 //use 'LODInfo' produced by IDS. it is used to search Ontology info,using LOD address that is saved in 'LODInfo'
		 this.LODInfo=model;
	}
	
	public void searchOntologyInfo(String fileName)
	{
		readIdsResults(fileName);
		getPredicateList();
		getTargetList();
		getOntologyInfo();
		writeOntology();
		
	}
	
	
	public void getPredicateList()
	{
		//Get predicate from results of IDS to search predicate's ontology
		Model idsResults=this.resultModel;
		 
        String queryString=
				"select distinct ?p"+
					" where {?s ?p ?o }";
        
        QueryExecution qexec = QueryExecutionFactory.create(queryString, idsResults);//쿼리 생성
        
        
        
        ResultSet results = qexec.execSelect();
    	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode predicate=soln.getResource("?p");
		      this.predicateList.add(predicate.toString());
		    }
        

	}
	
	public void getTargetList()
	{
		//get target LOD address from LOD Info
		Model LODInfo=this.LODInfo;
		
		String queryString="String distinct ?o where { ?s <http://linkpolicy.org/ontology/targetLOD> ?o }";
		

		QueryExecution qexec = QueryExecutionFactory.create(queryString, LODInfo);//쿼리 생성
        
        
        
        ResultSet results = qexec.execSelect();
    	
		 for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode targetLOD=soln.getResource("?o");
		      this.targetLODList.add(targetLOD.toString());
		    }
	}
	
	public void getOntologyInfo()
	{
		
		Searcher ontologySearcher=new Searcher();
		ResultSet results=null;
		for(int i=0; i<this.predicateList.size(); i++)
		{
			if(!checkURI(this.predicateList.get(i)))
			{
				System.out.println("Not available address");
				continue;
			}
			
			
			for(int k=0; k<this.targetLODList.size(); k++)
			{
				String query="select * where { <"+this.predicateList.get(i)+"> ?p ?o}";
				results=ontologySearcher.executeQuery(query, this.targetLODList.get(k));
				//use first method
				
				if(results==null)
				{
					//if firds method is fail, use second method us 'makeSparqlEndpoint()'
					results=ontologySearcher.executeQuery(query, makeSparqlEndpoint(this.predicateList.get(i)));
				
					if(results==null)
					{
						System.out.println("no ontology");
						continue;
					}
					else{
						break;
					}
				}
			}
			
			makeOntology(this.predicateList.get(i),results);
			//save ontology info
		}
		
		
		
	}
	

   public boolean checkURI(String uri)
	{
		try {
			URL url;
			url = new URL(uri);
			URLConnection con = url.openConnection();
			HttpURLConnection exitCode = (HttpURLConnection)con;
			exitCode.getResponseCode();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
   
   
   void makeOntology(String predicate,ResultSet results)
	{
	    //save ontology info by N-triple
		Resource resource=ontologyModel.createResource(predicate);
		try{
			for(; results.hasNext() ;)
			{
				QuerySolution soln =  results.nextSolution() ;
			    Resource predicate2 = soln.getResource("?p"); 
			    RDFNode object= soln.get("?o");
			    resource.addProperty(ontologyModel.createProperty(predicate2.toString()), object.toString());
			}
		}catch(Exception e)
		{
			return;
		}
	}
	
	
	
	public void writeOntology()
	{

		FileWriter out;
		try {
			out = new FileWriter("ResultOntology.nt");

			ontologyModel.write(out,"N-TRIPLES");
			out.close();
		} catch (IOException e) {
			
		}
		ontologyModel.write(System.out,"N-TRIPLES");

	}
	
	public String makeSparqlEndpoint(String tmp)
	{
	
		//make SPARQL Endpoint, using predicate address
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		StringTokenizer st = new StringTokenizer(tmp,"/"); 
		while (st.hasMoreTokens()){ 
		 tmp_str.add(st.nextToken()); 
		}
		
		
		return tmp_str.get(0)+"//"+tmp_str.get(1)+"/sparql";

	}
}
