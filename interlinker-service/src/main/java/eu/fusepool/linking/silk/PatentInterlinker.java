/*
 * Copyright 2014 Reto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.fusepool.linking.silk;

import eu.fusepool.datalifecycle.Interlinker;
import eu.fusepool.java.silk.client.SilkClient;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.Serializer;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;

@Component(immediate = true, metatype = true)
@Service(Interlinker.class)
@Properties({
    @Property(name = Constants.SERVICE_RANKING, 
    		  intValue = SilkInterlinker.DEFAULT_SERVICE_RANKING),
    @Property(name = SilkInterlinker.SPARQL_ENDPOINT_NAME,
              label= SilkInterlinker.SPARQL_ENDPOINT_LABEL,
    		  value = SilkInterlinker.DEFAULT_SPARQL_ENDPOINT, 
    		  description = SilkInterlinker.SPARQL_ENDPOINT_DESCRIPTION)})
public class PatentInterlinker extends SilkInterlinker {

    @Reference
    private Parser parser;
    
    @Reference
    private Serializer serializer;
    
    // Silk Interlinking service
    @Reference
    private SilkClient silk;
    
    @Activate
    @Override
    protected void activate(ComponentContext ce) throws IOException,
            ConfigurationException {
        super.activate(ce);

    }

    @Deactivate
    @Override
    protected void deactivate(ComponentContext context) {
        super.deactivate(context);
    }
    @Override
    public String getName() {
        return "silk-patents";
    }

    @Override
    protected InputStream getConfigTemplate() {
        return this.getClass().getResourceAsStream("silk.config-patents.xml");
    }

    public Parser getParser() {
        return parser;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public SilkClient getSilk() {
        return silk;
    }
    
    
}
