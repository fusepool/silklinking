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

import java.io.File;
import java.io.InputStream;

/**
 * @author giorgio
 *
 */
public interface SilkClient {

    /**
     * Executes loading a default configuration
     *
     * @throws Exception
     */
    void excute() throws SilkException;

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
     *
     * @param config
     * @param linkSpecId
     * @param numThreads
     * @param reload
     * @throws Exception
     */
    void executeFile(File config, String linkSpecId, int numThreads, boolean reload) throws SilkException;

}
