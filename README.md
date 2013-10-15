Silk Wrapper
===========

This project wraps the SILK Link Discovery Framework to provide an OSGi bundle with an RDF to RDF interlinking service for the Apache Stanbol platform.
This component provides a service for the Silk Linking Engine component so this must be previously installed in the OSGi environment. To compile the bundle run the command

    mvn install

Then install the bundle created in the silk-fusepool-bundle/target folder in the OSGi environment. After the bundle has been installed you have to compile and install
the Silk Linking Engine component that provides the interface to be used by other components or client for the interlinking task. 
