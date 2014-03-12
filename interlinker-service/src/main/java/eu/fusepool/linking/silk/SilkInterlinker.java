/**
 *
 */
package eu.fusepool.linking.silk;

import java.io.IOException;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Deactivate;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.fusepool.datalifecycle.Interlinker;
import eu.fusepool.java.silk.client.SilkClient;
import eu.fusepool.java.silk.client.SilkException;
import java.io.InputStream;

/**
 * @author giorgio
 * @author luigi
 *
 */
public abstract class SilkInterlinker implements Interlinker {

    private static Logger logger = LoggerFactory.getLogger(SilkInterlinker.class);

    /**
     * Default value for the {@link Constants#SERVICE_RANKING} used by this
     * engine. This is a negative value to allow easy replacement by this engine
     * depending to a remote service with one that does not have this
     * requirement   
     */
    public static final int DEFAULT_SERVICE_RANKING = 101;

    @Activate
    protected void activate(ComponentContext ce) throws IOException,
            ConfigurationException {
        logger.info("The {} Silk Linking Service is being activated.", getName());

    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        logger.info("The Silk Linking Service is being deactivated");
    }

    public TripleCollection interlink(TripleCollection dataToInterlink, UriRef targetGraphRef) {
        try {
            TripleCollection result = getSilk().executeStream(dataToInterlink, targetGraphRef, getConfigTemplate(), null, 1, true);
            //the output already omits self-identity and simmetric triples
            //LinkUtils.removeInferenceableEquivalences(result);
            return result;
        } catch (SilkException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public TripleCollection interlink(TripleCollection source, TripleCollection target) {
        try {
            TripleCollection result = getSilk().executeStream(source, target, getConfigTemplate(), null, 1, true);
            //the output already omits self-identity and simmetric triples
            //LinkUtils.removeInferenceableEquivalences(result);
            return result;
        } catch (SilkException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    // name set in component configuration panel
    public abstract String getName();
    
    protected abstract InputStream getConfigTemplate();

    protected abstract SilkClient getSilk();
    
    
}
