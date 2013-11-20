Silk Interlinking Service
===========

This project wraps the SILK Link Discovery Framework to provide OSGi components an RDF to RDF interlinking service. 
To compile the bundle run the command

    mvn install

This bundle implements the Interlinker interface (eu.fusepool.datalifecycle.Interlinker, see fusepool/datalifecycle 
repository on Github). The implemented method can be called to interlink a set of RDF triples against an SPARQL endpoint. 
The project has four submodules:

  1. silk-wrapper 
  2. silk-fusepool-api  
  3. silk-fusepool-bundle
  4. interlinker-service

The silk-wrapper module wraps the scala code in which Silk is implemented. The silk-fusepool-api declares an interface 
to access methods calls provided by Silk and  silk-fusepool-bundle provides an implementation. The interlinker-service 
provides an implementation of the Interlinker interface to make the interlinking service based on Silk or any other future 
interlinking service, available to other components. It lies in between silk-fusepool-bundle component and any other 
component that needs an interlinking service. The silk-fusepool-bundle component must be installed in the OSGi framework 
from the silk-fusepool-bundle/target folder. The interlinker-service component must be installed from interlinker-service/target 
folder. The datalifecycle component is an application that uses the interlinking service implemented by silk-fusepool-bundle 
component through the interlinker-service component. The silk-fusepool-bundle can also be used directly, without using the 
Interlinker interface, by the silklinkingengine bundle. See that project for more info.


