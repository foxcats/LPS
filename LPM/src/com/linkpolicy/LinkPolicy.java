package com.linkpolicy;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.shared.InvalidPropertyURIException;

public class LinkPolicy {
	
	private Model model;
	private Resource sourceLOD;
	private String sourceUri;
	
	public LinkPolicy(String sourceUri)
	{	

		this.sourceUri=sourceUri;
	    model=ModelFactory.createDefaultModel();
		sourceLOD=model.createResource(sourceUri);
		
	}
	
	
	public Resource makeLinkPolicy(String targetLOD,String targetLinkPolicyAddress)
	{
		Resource blankNode=model.createResource();
		Property linkPolicyProperty=model.createProperty("http://linkpolicy.org/ontology/linkpolicy");
		sourceLOD.addProperty(linkPolicyProperty,blankNode);
		
		Property targetLODProperty=model.createProperty("http://linkpolicy.org/ontology/targetLOD");
		Property linkPolicyAddress=model.createProperty("http://linkpolicy.org/ontology/targetLinkPolicyAddress");
		blankNode.addProperty(targetLODProperty, targetLOD);
		blankNode.addProperty(linkPolicyAddress, targetLinkPolicyAddress);
		return blankNode;
		
	}
	
	public Resource insertTypePredicate(Resource blankNode,String sourceTypePredicate,String targetTypePredicate,
			String sourceType,String targetType)
	{
		
		Property restrictPredicateMatch=model.createProperty("http://linkpolicy.org/ontology/restrictPredicateMatch");
		Property targetRestrcit=model.createProperty("http://linkpolicy.org/ontology/targetRestric");
		Property sourceRestricPredicate=model.createProperty("http://linkpolicy.org/ontology/sourceRestricPredicate");
		Property sourceRestricType=model.createProperty("http://linkpolicy.org/ontology/sourceRistricType");
		Property targetRistricPredicate=model.createProperty("http://linkpolicy.org/ontology/targetRistricPredicate");
		Property targetRestricType=model.createProperty("http://linkpolicy.org/ontology/targetRistricType");
		
		Resource blankNode2=model.createResource();
		Resource blankNode3=model.createResource();
		blankNode2.addProperty(targetRestrcit, blankNode3);
		blankNode2.addProperty(sourceRestricPredicate, sourceTypePredicate);
		blankNode2.addProperty(sourceRestricType, sourceType);
		
		blankNode3.addProperty(targetRistricPredicate, targetTypePredicate);
		blankNode3.addProperty(targetRestricType, targetType);
		
		blankNode.addProperty(restrictPredicateMatch, blankNode2);
		
		return blankNode2;
		
		
	}
	
	
	public Resource insertLinkProperty(Resource blankNode,String sourcePredicate, String targetPredicate)
	{
		
		//Resource blankNode4=model.createResource();
		Property sourcePredicateProperty=model.createProperty("http://linkpolicy.org/ontology/sourcePredicate");
		Property targetPredicateProperty=model.createProperty("http://linkpolicy.org/ontology/targetPredicate");
		Property predicateMatching=model.createProperty("http://linkpolicy.org/ontology/preidcateMatching");
		Resource blankNode2=model.createResource();
		
		blankNode.addProperty(predicateMatching,blankNode2);
		blankNode2.addProperty(sourcePredicateProperty,sourcePredicate);
		blankNode2.addProperty(targetPredicateProperty, targetPredicate);
	
		return blankNode2;
	}
	
	public void insertDuplicationLinkProperty(Resource predicateMatchingNode, String targetPredicate)
	{
		
		Property targetPredicateProperty=model.createProperty("http://linkpolicy.org/ontology/targetPredicate");
		
		predicateMatchingNode.addProperty(targetPredicateProperty, targetPredicate);
		
	}

	public void showLinkPolicy()
	{
		model.write(System.out, "RDF/XML-ABBREV");
	}
	
	public void outputFile(String linkPolicyName)
	{
		 FileWriter out = null;
			try {
				
				System.out.println("RDF 기록");
				out = new FileWriter(linkPolicyName+".rdf");

				model.write(out,"RDF/XML-ABBREV");
				
				out.close();
				
			} catch (IOException e) {
				
			}
			catch(InvalidPropertyURIException e)
			{
				System.out.println("에러");
			}
	}
}
