# CBIDS, OS and LPM


OS is the searching API to get datas that have ontology Info using in CBIDS'z RDF results like 'predicate'.

LPM is the Link-Policy maker used in LPS.


When you use IDS, OS and LPM,
You must import jena API in 'apache-jena-3.1.0.zip'

Also, in case of using LPS, You must imort 'java-string-similarity-0.21.jar' aditionally to campare Object in triple.





# CBIDS(Cycle based In-Depth Searching)
CBIDS is the searching API to get RDF datas in LODs using Link-Policy-Link and Explicit-Link.

Link-Policy-Link use Link-Policy declared by user that has the information to link another LODs.

The information is comprised by 'Topic Restriction Specification' and 'Predicate Matching Specification'

Topic Restriction Specification decide entity's topic that is RDF Triples to get results of IDS in source LOD for searching same entitys at target LODs linked with source LOD



For example, suppose  <http://dbpedia.org/ontology/Film> is registied by Topic Restriction Specification about sourceLOD and targetLOD's topic is <http://dbpedia.org/ontology/Film> that is linked with above topic.
IDS for LPS will check source entity's topic, if it is  <http://dbpedia.org/ontology/Film> and it is correct and there start to search same entitys in target LOD whose topic is <http://dbpedia.org/ontology/Film>.

Predicate Matching Specifiation is predicate informations about source LOD and target LOD that is regitried by publisher who publish sourceLOD in LOD Cloud when he decide that these predicates have same meaning each other.

Predicate Matching Specification is subordinate to Topic Restriction Specification.
this mean that Predicate Mathching Specification takes effect when source entity and target entity are matched to Topic Restriction Specification with whitch Predicate Matching Specificatio is linked.

CBIDS also support EC(Entity Confidence) that means that How user can trust Entity that searched by IDS

if users set ec they want, CBIDS clean results that  coincide with ec

if you want to check Link-Policy, Please download Link-Policy in CBIDS folder (ko.dbpedia.org_linkPolicy.rdf,fr.dbpedia.org_linkPolicy.rdf...)

# OS(Ontology Searcher)

We serve Ontology Searcher to recognize predicate Information in IDS results of RDF triple.



# LPM (Link-Policy Maker)

if you want to make Link-Policy

Please make file name  using SPARQL Endpoint

For example,
http://ko.dbpeda.org/sparql-> ko.dbpedia.org_linkPolicy.rdf
