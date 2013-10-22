/**
 * 
 */
package eu.fusepool.enhancer.linking;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Map;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.Triple;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fusepool.datalifecycle.Interlinker;
import eu.fusepool.enhancer.linking.silkjob.SilkJob;

/**
 * @author giorgio
 * 
 */
@Component(immediate = true, metatype = true, configurationFactory = true, // allow
																			// multiple
																			// instances
policy = ConfigurationPolicy.OPTIONAL)
// create a default instance with the default configuration
@Service
@Properties(value = {
		@Property(name = Constants.SERVICE_RANKING, intValue = PatentLinkingService.DEFAULT_SERVICE_RANKING),
		@Property(name = PatentLinkingService.SPARQL_ENDPOINT_LABEL, value = "", description = "SPARQL endpoint of the target (master) repository") })
public class PatentLinkingService implements Interlinker {

	private static Logger logger = LoggerFactory
			.getLogger(PatentLinkingService.class);

	/**
	 * Default value for the {@link Constants#SERVICE_RANKING} used by this
	 * engine. This is a negative value to allow easy replacement by this engine
	 * depending to a remote service with one that does not have this
	 * requirement
	 */
	public static final int DEFAULT_SERVICE_RANKING = 101;

	public static final String DEFAULT_SPARQL_ENDPOINT = "http://localhost:8080/sparql";

	// Labels for the component configuration panel
	public static final String SPARQL_ENDPOINT_LABEL = "Endpoint";

	protected ComponentContext componentContext;
	protected BundleContext bundleContext;

	String sparqlEndpoint = this.DEFAULT_SPARQL_ENDPOINT;

	String sparqlGraph = "";

	private final static boolean isDebug = true;

	
	@Activate
	protected void activate(ComponentContext ce) throws IOException,
			ConfigurationException {
		this.componentContext = ce;
		this.bundleContext = ce.getBundleContext();

		@SuppressWarnings("rawtypes")
		Dictionary dict = ce.getProperties();
		Object o = dict.get(PatentLinkingService.SPARQL_ENDPOINT_LABEL);
		if (o != null && !"".equals(o.toString())) {
			sparqlEndpoint = (String) o;
		}

		logger.info("The SilkLinkingService engine is being activated");

	}

	@Deactivate
	protected void deactivate(ComponentContext context) {
		logger.info("The SilkLinkingService engine is being deactivated");
	}

	public TripleCollection interlink(TripleCollection dataToInterlink,
			UriRef interlinkAgainst) {
		SilkJob job = new SilkJob(bundleContext, sparqlEndpoint, interlinkAgainst);
		return job.executeJob(dataToInterlink);
	}

}
