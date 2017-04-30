# IDS, OS and LPM


Os is the searching API to get datas that have ontology Info using in LPS RDF results like 'predicate'.

LPM is the Link-Policy maker used in LPS.


When you use IDS, OS and LPM,
You must import jena API in 'apache-jena-3.1.0.zip'

Also, in case of using LPS, You must imort 'java-string-similarity-0.21.jar' aditionally to campare Object in triple.





# IDS(In-Depth-Searching)
IDS is the searching API to get RDF datas in LODs using Link-Policy-Link and Explicit-Link.

Link-Policy-Link use Link-Policy declared by user that has the information to link another LODs.

The information is comprised by 'Topic Restriction Specification' and 'Predicate Matching Specification'

Topic Restriction Specification decide entity's topic that is RDF Triples to get results of IDS in source LOD for searching same entitys at target LODs linked with source LOD



For example, suppose  <http://dbpedia.org/ontology/Film> is registied by Topic Restriction Specification about sourceLOD and targetLOD's topic is <http://dbpedia.org/ontology/Film> that is linked with above topic.
IDS for LPS will check source entity's topic, if it is  <http://dbpedia.org/ontology/Film> and it is correct and there start to search same entitys in target LOD whose topic is <http://dbpedia.org/ontology/Film>.

Predicate Matching Specifiation is predicate informations about source LOD and target LOD that is regitried by publisher who publish sourceLOD in LOD Cloud when he decide that these predicates have same meaning each other.

Predicate Matching Specification is subordinate to Topic Restriction Specification.
this mean that Predicate Mathching Specification takes effect when source entity and target entity are matched to Topic Restriction Specification with whitch Predicate Matching Specificatio is linked.

this is example of Link-Policy


<rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:j.0="http://linkpolicy.org/ontology/">
  <rdf:Description rdf:about="http://fr.dbpedia.org/sparql">
    <j.0:linkpolicy rdf:parseType="Resource">
      <j.0:restrictPredicateMatch rdf:parseType="Resource">
        <j.0:preidcateMatching rdf:parseType="Resource">
          <j.0:targetPredicate>http://it.dbpedia.org/property/annouscita</j.0:targetPredicate>
          <j.0:sourcePredicate>http://fr.dbpedia.org/property/annéeDeSortie</j.0:sourcePredicate>
        </j.0:preidcateMatching>
        <j.0:preidcateMatching rdf:parseType="Resource">
          <j.0:targetPredicate>http://it.dbpedia.org/property/titolooriginale</j.0:targetPredicate>
          <j.0:sourcePredicate>http://fr.dbpedia.org/property/commonsTitre</j.0:sourcePredicate>
        </j.0:preidcateMatching>
        <j.0:preidcateMatching rdf:parseType="Resource">
          <j.0:targetPredicate>http://it.dbpedia.org/property/titolooriginale</j.0:targetPredicate>
          <j.0:sourcePredicate>http://fr.dbpedia.org/property/titreOriginal</j.0:sourcePredicate>
        </j.0:preidcateMatching>
        <j.0:preidcateMatching rdf:parseType="Resource">
          <j.0:targetPredicate>http://it.dbpedia.org/property/titolooriginale</j.0:targetPredicate>
          <j.0:sourcePredicate>http://fr.dbpedia.org/property/titre</j.0:sourcePredicate>
        </j.0:preidcateMatching>
        <j.0:sourceRistricType>http://dbpedia.org/ontology/Film</j.0:sourceRistricType>
        <j.0:sourceRestricPredicate>http://www.w3.org/1999/02/22-rdf-syntax-ns#type</j.0:sourceRestricPredicate>
        <j.0:targetRestric rdf:parseType="Resource">
          <j.0:targetRistricType>http://dbpedia.org/ontology/Film</j.0:targetRistricType>
          <j.0:targetRistricPredicate>http://www.w3.org/1999/02/22-rdf-syntax-ns#type</j.0:targetRistricPredicate>
        </j.0:targetRestric>
      </j.0:restrictPredicateMatch>
      <j.0:targetLinkPolicyAddress>IT_DBpedia_linkPolicy</j.0:targetLinkPolicyAddress>
      <j.0:targetLOD>http://it.dbpedia.org/sparql</j.0:targetLOD>
    </j.0:linkpolicy>
  </rdf:Description>
</rdf:RDF>

this Link-Policy show linking between DBpedia of Frange and DBpedia of Itary

#LPM 
if you want to make Link-Policy

Please make file name  using SPARQL Endpoint

For example,
http://ko.dbpeda.org/sparql-> ko.dbpedia.org_linkpolicy.rdf
