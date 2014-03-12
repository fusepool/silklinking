/*
 * Copyright 2013 Fusepool.
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
package eu.fusepool.java.silk.client;

import java.io.InputStream;
import org.apache.clerezza.rdf.core.TripleCollection;
import org.apache.clerezza.rdf.core.UriRef;

/**
 * @author giorgio
 *
 */
public interface SilkClient {


    /**
     *
     * @param config
     * @param linkSpecId
     * @param numThreads
     * @param reload
     * @throws Exception
     */
    void executeStream(InputStream config, String linkSpecId, int numThreads, boolean reload) throws SilkException;
    
    /**
     * Takes source and target graphs and returns a graph with the interlinks. 
     * The config must use DataSources of type fpSource and fpTarget and use fpWriter as output
     */
    TripleCollection executeStream(TripleCollection sourceGraph, UriRef targetGraphName, 
            InputStream config, String linkSpecId, int numThreads, boolean reload) throws SilkException;
    
    TripleCollection executeStream(TripleCollection sourceGraph, TripleCollection targetGraph, 
            InputStream config, String linkSpecId, int numThreads, boolean reload) throws SilkException;




}
