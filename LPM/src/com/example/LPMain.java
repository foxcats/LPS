package com.example;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.InvalidPropertyURIException;

import com.linkpolicy.LinkPolicy;

public class LPMain {

	
	public static void main(String[] args)
	{
		
		LinkPolicy linkpolicy=new LinkPolicy("http://es.dbpedia.org/sparql");
		 Resource blankNode=linkpolicy.makeLinkPolicy("http://de.dbpedia.org/sparql","DE_DBpedia_linkPolicy");
		 Resource blankNode2=linkpolicy.insertTypePredicate(blankNode, "http://www.w3.org/1999/02/22-rdf-syntax-ns#type",
				 "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", "http://dbpedia.org/ontology/Film",
				 "http://dbpedia.org/ontology/Film");
		 linkpolicy.insertLinkProperty(blankNode2,"http://es.dbpedia.org/property/títuloOriginal","http://de.dbpedia.org/property/ot");
		 Resource blankNode3=linkpolicy.insertLinkProperty(blankNode2,"http://es.dbpedia.org/property/estreno","http://de.dbpedia.org/property/pj");
		 Resource blankNode4=linkpolicy.insertLinkProperty(blankNode2,"http://es.dbpedia.org/property/año","http://de.dbpedia.org/property/pj");
		 linkpolicy.insertDuplicationLinkProperty(blankNode3,"http://de.dbpedia.org/property/ej");
		 linkpolicy.insertDuplicationLinkProperty(blankNode4,"http://de.dbpedia.org/property/ej");
		 linkpolicy.showLinkPolicy();
		 
		 linkpolicy.outputFile("ES_DBpedia_linkPolicy");
	}
}
