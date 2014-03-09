package eu.fusepool.linking.silk;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.NonLiteral;
import org.apache.clerezza.rdf.core.Resource;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.ontologies.OWL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkUtils {
    
    private static Logger logger = LoggerFactory.getLogger(LinkUtils.class);
    /**
     * Removes owl:sameAs triples where subject and object have the same representation
     * (e.g. <uri1><owl:sameAs><uri1>
     * Removes triples that can be inferred by similarity property of owl:sameAs. Removes one of
     * {<uri1><owl:sameAs><uri2>, <uri2><owl:sameAs><uri1>}
     * @param graph
     */
    public static void removeInferenceableEquivalences(TripleCollection graph) {
        //removes equivalences that can be inferred by reflexivity of owl:sameAs
        MGraph reflexGraph = new SimpleMGraph();
        int initialSize = graph.size();
        logger.info("Removing inferenceable triple from " + initialSize + " equivalences.");
        Iterator<Triple> equivalences = graph.filter(null, OWL.sameAs, null);
        while(equivalences.hasNext()) {
            Triple equivalence = equivalences.next();
            Resource subject = (Resource) equivalence.getSubject();
            Resource object = equivalence.getObject();
            if(subject.equals(object)){
                reflexGraph.add(equivalence);
            } 
        }
        
        graph.removeAll(reflexGraph);
        logger.info("Removed " + reflexGraph.size() + " inferenceable equivalences by owl:sameAs reflexivity.");
        
        //removes equivalences that can be inferred by symmetry of owl:sameAs
        // TO-DO
        
    }
 
}
