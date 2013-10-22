Silk Linking Engine
=================

An enhancement engine for interlinking that uses the services provided by the SILK Wrapper bundle (silklinking project). The wrapper must be 
already installed in the OSGi environment in order to use this component for the interlinking task. This component provides an interface to be 
used by other engines or client for the interlinking service. The Silk Linking Engine compares RDF data with a master repository via its SPARQL 
endpoint. The master (target) repository is set in the SILK configuration file.

To compile the bundle run the following command

    mvn install

Then install the bundle created in the target folder in the OSGi environment.
Here follow two examples on how to use or test the bundle:
- send the rdf data to a chain

    curl -u user:password -i -X POST -H "Accept: text/turtle" -H "Content-Type: application/rdf+xml" -T <rdf_file> "http://platform.fusepool.info/enhancer/chain/silk_chain"
    

- send the data directly to the engine

    curl -u admin:admin -X POST -H "Accept: text/turtle" -H "Content-type: application/rdf+xml" -T <rdf_file> http://platform.fusepool.info/enhancer/engine/PatentLinkingEnhancer
