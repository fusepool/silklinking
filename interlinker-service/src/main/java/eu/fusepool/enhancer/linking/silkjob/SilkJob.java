/**
 *
 */
package eu.fusepool.enhancer.linking.silkjob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.clerezza.rdf.core.serializedform.SerializingProvider;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.stanbol.commons.indexedgraph.IndexedMGraph;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fusepool.java.silk.client.SilkClient;
import eu.fusepool.java.silk.client.SilkException;
import java.util.logging.Level;

/**
 * @author giorgio
 *
 */
public class SilkJob {

    private static final Logger logger = LoggerFactory.getLogger(SilkJob.class);

    private static final String CI_METADATA_TAG = "[CI_METADATA_FILE]";
    private static final String OUTPUT_TMP_TAG = "[OUTPUT_TMP_FILE_PATH]";
    private static final String SPARQL_ENDPOINT_01_TAG = "[SPARQL_ENDPOINT_01]";
    private static final String SPARQL_GRAPH_01_TAG = "[GRAPH_PARAMETER]";

    // sparql endpoint
    String sparqlEndpoint;
    String sparqlGraph;

    private static final boolean holdDataFiles = true;
    // private boolean stick2RDF_XML = false ;

    protected BundleContext bundleContext;

    private String jobId;
    SilkClient silk;
    Parser parser;
    SerializingProvider provider = null;
    Serializer serializer;
    File rdfData; // source RDF data
    File outputData;

    String config;

    public SilkJob(BundleContext ctx, String sparqlEndpoint, UriRef graphName) {
        bundleContext = ctx;
        this.sparqlEndpoint = sparqlEndpoint;
        this.sparqlGraph = graphName.getUnicodeString();
        logger.info("silk job started");
    }

    @SuppressWarnings("unused")
    public MGraph executeJob(TripleCollection dataToInterlink) {

        jobId = UUID.randomUUID().toString();
        try {
            TripleCollection inputGraph = null;
            getSilkClient();
            getParser();
            getSerializer();
            createTempFiles();

            OutputStream rdfOS = new FileOutputStream(rdfData);
            provider.serialize(rdfOS, dataToInterlink, SupportedFormat.RDF_XML);
            rdfOS.close();
            buildConfig();

            silk.executeStream(IOUtils.toInputStream(config, "UTF-8"), null, 1,
                    true);

			// This graph will contain the results of the duplicate detection
            // i.e. owl:sameAs statements
            MGraph owlSameAsStatements = new IndexedMGraph();
            InputStream is = new FileInputStream(outputData);

            Set<String> formats = parser.getSupportedFormats();
            parser.parse(owlSameAsStatements, is, SupportedFormat.N_TRIPLE);
            is.close();
            logger.info(owlSameAsStatements.size()
                    + " triples extracted by job: " + jobId);
            return owlSameAsStatements;

        } catch (IOException e) {
            throw new RuntimeException("Exception creating the silk files", e);
        } catch (SilkException e) {
            throw new RuntimeException(e);
        } finally {
            cleanUpFiles();
        }
    }

    private void createTempFiles() {
        String ifName = jobId + "-in.rdf";
        String ofName = jobId + "-out.rdf";
        rdfData = bundleContext.getDataFile(ifName);
        outputData = bundleContext.getDataFile(ofName);
    }

    private void getSilkClient() {
        String className = SilkClient.class.getName();
        ServiceReference ref = bundleContext.getServiceReference(className);
        if (ref != null) {
            silk = (SilkClient) bundleContext.getService(ref);
        } else {
            silk = null;
			// logger.error("Cannot get SilkClient Service for the job: "+jobId)
            // ;
            System.out.println("Cannot get SilkClient Service for the job: "
                    + jobId);
            throw new RuntimeException("Cannot get SilkClient Service!");
        }
    }

    private void getParser() {
        ServiceReference ref = bundleContext.getServiceReference(Parser.class
                .getName());
        if (ref != null) {
            parser = (Parser) bundleContext.getService(ref);
        } else {
            parser = null;
            // logger.error("Unable to find parser for the job: "+jobId) ;
            System.out.println("Unable to find parser for the job: " + jobId);
            throw new RuntimeException("Cannot get parser!");
        }
    }

    /**
     * builds the configuration for the silk job
     *
     * @throws IOException
     */
    private void buildConfig() throws IOException {
        InputStream cfgIs = this.getClass().getResourceAsStream(
                "/silk-config-applicants-test.xml");
        String roughConfig = IOUtils.toString(cfgIs, "UTF-8");
        roughConfig = StringUtils.replace(roughConfig, SPARQL_ENDPOINT_01_TAG,
                sparqlEndpoint);
        if (sparqlGraph != null && !"".equals(sparqlGraph)) {
            String graphParamFragment = "<Param name=\"graph\" value=\""
                    + sparqlGraph + "\"" + "></Param>";
            roughConfig = StringUtils.replace(roughConfig, SPARQL_GRAPH_01_TAG,
                    graphParamFragment);
        } else {
            roughConfig = StringUtils.replace(roughConfig, SPARQL_GRAPH_01_TAG,
                    "");
        }
        roughConfig = StringUtils.replace(roughConfig, CI_METADATA_TAG,
                rdfData.getAbsolutePath());
        config = StringUtils.replace(roughConfig, OUTPUT_TMP_TAG,
                outputData.getAbsolutePath());
        // logger.info("configuration built for the job:" + jobId+"\n"+config) ;
        System.out.println("configuration built for the job:" + jobId + "\n"
                + config);
    }

    private void getSerializer() {
        ServiceReference ref = bundleContext.getServiceReference(Serializer.class
                .getName());
        if (ref != null) {
            serializer = (Serializer) bundleContext.getService(ref);
        } else {
            serializer = null;
            // logger.error("Unable to find parser for the job: "+jobId) ;
            System.out.println("Unable to find parser for the job: " + jobId);
            throw new RuntimeException("Cannot get parser!");
        }
    }

    /**
     * Removes temporary files
     */
    private void cleanUpFiles() {
        if (holdDataFiles) {
            return;
        }
        if (rdfData != null) {
            rdfData.delete();
        }
        if (outputData != null) {
            outputData.delete();
        }
    }

    /**
     * @return the sparqlEndpoint
     */
    public String getSparqlEndpoint() {
        return sparqlEndpoint;
    }

    /**
     * @param sparqlEndpoint the sparqlEndpoint to set
     */
    public void setSparqlEndpoint(String sparqlEndpoint) {
        this.sparqlEndpoint = sparqlEndpoint;
    }

    /**
     * @return the sparqlGraph
     */
    public String getSparqlGraph() {
        return sparqlGraph;
    }

    /**
     * @param sparqlGraph the sparqlGraph to set
     */
    public void setSparqlGraph(String sparqlGraph) {
        this.sparqlGraph = sparqlGraph;
    }
}
