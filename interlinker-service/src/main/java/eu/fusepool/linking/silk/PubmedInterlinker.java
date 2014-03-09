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
import java.io.InputStream;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.framework.Constants;

@Component(immediate = true, metatype = true)
@Service(Interlinker.class)
@Properties({
    @Property(name = Constants.SERVICE_RANKING, 
    		  intValue = SilkInterlinker.DEFAULT_SERVICE_RANKING),
  })
public class PubmedInterlinker extends SilkInterlinker {


    // Silk Interlinking service
    @Reference
    private SilkClient silk;
    
    @Override
    public String getName() {
        return "silk-pubmed";
    }

    @Override
    protected InputStream getConfigTemplate() {
        return this.getClass().getResourceAsStream("silk-config-pubmed-agents.xml");
    }


    public SilkClient getSilk() {
        return silk;
    }
    
    
}
