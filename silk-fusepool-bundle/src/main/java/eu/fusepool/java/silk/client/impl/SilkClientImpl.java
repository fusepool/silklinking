/**
 *
 */
package eu.fusepool.java.silk.client.impl;

import java.io.InputStream;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;

import eu.fusepool.java.silk.client.SilkClient;
import eu.fusepool.java.silk.client.SilkException;
import eu.fusepool.scala.silk.Silk;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.access.TcManager;
import org.apache.felix.scr.annotations.Reference;
import org.apache.stanbol.commons.indexedgraph.IndexedMGraph;

/**
 * @author giorgio
 *
 */
@Component(immediate = true, metatype = true)
@Service(SilkClient.class)
public class SilkClientImpl implements SilkClient {

    
    @Reference
    private TcManager tcManager;
    
    
    @Override
    public void executeStream(InputStream config, String linkSpecId,
            int numThreads, boolean reload) {
        Silk.executeStream(config, linkSpecId, numThreads, reload);
    }
    
    @Override
    public TripleCollection executeStream(TripleCollection sourceGraph, UriRef targetGraphName, InputStream config, String linkSpecId,
            int numThreads, boolean reload) {
        MGraph output = new IndexedMGraph();
        Silk.executeStream(sourceGraph, targetGraphName, tcManager, output, config, linkSpecId, numThreads, reload);
        return output;
    }
    
    @Override
    public TripleCollection executeStream(TripleCollection sourceGraph, TripleCollection targetGraph, 
            InputStream config, String linkSpecId, int numThreads, boolean reload) throws SilkException {
        MGraph output = new IndexedMGraph();
        Silk.executeStream(sourceGraph, targetGraph, output, config, linkSpecId, numThreads, reload);
        return output;
    }


}
